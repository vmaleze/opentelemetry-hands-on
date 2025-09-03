# Hands On - OpenTelemetry

## Prerequisites

- OS session with administrator permissions
- IDE like Intellij or VS Code
- PowerShell 7 or Bash
- Docker
- Git and a GitHub account
- [mise](https://mise.jdx.dev/getting-started.html)
- [Headlamp](https://headlamp.dev/)

## Repository content

- microservices: 4 Spring Boot applications
- observability: configuration of the observability stack
- presentation-slides: the presentation in format [Slidev](https://sli.dev/)
- signoz: configuration of SigNoz
- traffic-simulation: script to simulate the traffic on the microservices

The presentation slides are available on [GitHub Pages](https://vmaleze.github.io/opentelemetry-hands-on/).

## Configuration for MacOS

## Configuration for Linux

## Configuration for Windows with PowerShell

### Install winget

It is normally included in recent Windows 10 versions and in Windows 11. If not, check the [Microsoft documentatiopn](https://learn.microsoft.com/fr-fr/windows/package-manager/winget/).

### Install Docker Desktop

[Installation guide](https://docs.docker.com/desktop/setup/install/windows-install/)

### Install mise

[Starting documenation](https://mise.jdx.dev/getting-started.html)

From the directory when the project was cloned:

```powershell
winget install jdx.mise
Invoke-Item $profile
# Paste the content to [activate mise](https://mise.jdx.dev/getting-started.html#activate-mise)
mise install
```

Everything is stored in `%LocalAppData%\mise`.

### Install Headlamp

[Officiel website](https://headlamp.dev/)

```powershell
winget install headlamp
```

Switch to "English" in the settings.

## TP 1: start the application to find a bug

### Cluster configuration

```sh
k3d cluster create otel-hands-on --agents 2 --port "80:80@loadbalancer" --port "443:443@loadbalancer"
kubectl create namespace microservices
kubens microservices

### Kafka
kubectl apply -n microservices -f microservices/infra/kafka-deployment.yaml

### SigNoz
helm repo add signoz https://charts.signoz.io
helm repo update
helm install signoz signoz/signoz --namespace observability --create-namespace -f signoz/values.yaml
```

Check in Headlamp that everything is fine.

If you need to relaunch the cluster:

```sh
k3d cluster start otel-hands-on
```

### Microservices installation

```sh
helm dependency up ./microservices/order/infra && helm upgrade --install order ./microservices/order/infra
helm dependency up ./microservices/product/infra && helm upgrade --install product ./microservices/product/infra
helm dependency up ./microservices/shopping-cart/infra && helm upgrade --install shopping-cart ./microservices/shopping-cart/infra
helm dependency up ./microservices/stock/infra && helm upgrade --install stock ./microservices/stock/infra
```

Modify your hosts file with the DNS entries for each microservice:

- On MacOS or Linux, open "/etc/hosts" with any text editor, **as "sudo"**.
- On Windows, open "%SystemRoot%\System32\drivers\etc\hosts" with any text editor, **launched as an administrator**.
  - Either [enable sudo](https://learn.microsoft.com/en-us/windows/advanced-settings/sudo/#how-to-enable-sudo-for-windows), and then run `sudo notepad`
  - Or right click on Notepad and choose "Run as admnistrator"
- Add the following line to "hosts":

```txt
127.0.0.1 order.k3s.local product.k3s.local shopping-cart.k3s.local stock.k3s.local signoz.k3s.local
```

Check in Headlamp that everything is fine.

You can reach the APIs documentation at the following URLs: [order](https://order.k3s.local/swagger-ui/index.html), [product](https://product.k3s.local/swagger-ui/index.html), [shopping-cart](https://shopping-cart.k3s.local/swagger-ui/index.html), [stock](https://stock.k3s.local/swagger-ui/index.html). A certificate warning from the browser is expected.

### Launch order simulation

```sh
kubectl apply -f ./traffic-simulation/traffic-simulation-pod.yaml
```

The traffic simulation stops automatically after 10 minutes.

### Find a bug

There's a bug in the supply chain order: someone came to tell us that the order flow is suprislingly low and likes us to investigate. Can you find what's wrong?

Tip: check logs in Headlamp or via `kubectl get logs deployments/<deployment-name> -f`.

## TP 2: install the observability stack

### Install the operator

It will help us to install all OpenTelemetry resources.

```sh
kubectl create ns opentelemetry-operator-system
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.18.2/cert-manager.yaml
kubectl -n opentelemetry-operator-system apply -f https://github.com/open-telemetry/opentelemetry-operator/releases/latest/download/opentelemetry-operator.yaml
```

### Install the backend

```sh
kubectl apply -f signoz/ingress.yaml
```

### Install the collector

- Create an "observability" directory.
- Create an "otel-collector.yaml" file with:

```yaml
apiVersion: opentelemetry.io/v1beta1
kind: OpenTelemetryCollector
metadata:
  name: otel
  namespace: microservices
spec:
  mode: daemonset # Choose how to deploy the collector, cf https://github.com/open-telemetry/opentelemetry-operator/blob/main/README.md#deployment-modes
  image: otel/opentelemetry-collector-contrib:0.133.0
  config:
    receivers: # How to receive Open Telemetry data.
      otlp:
        protocols:
          grpc: {}
          http: {}

    exporters: # Where to export Open Telemetry data.
      otlp/signoz:
        endpoint: signoz-otel-collector.observability:4317
        tls:
          insecure: true

    service: # Configure each pipeline of Open Telemetry data.
      pipelines:
        logs:
          receivers: [otlp]
          exporters: [otlp/signoz]
        traces:
          receivers: [otlp]
          exporters: [otlp/signoz]
        metrics:
          receivers: [otlp]
          exporters: [otlp/signoz]
```

- Install the collector:

```sh
kubectl apply -f observability/otel-collector.yaml
```

### Install the java agent

- Create an "otel-instrumentation.yaml" file with:

```yaml
apiVersion: opentelemetry.io/v1alpha1
kind: Instrumentation
metadata:
  name: otel-auto-instrumentation
  namespace: opentelemetry-operator-system
spec:
  exporter:
    endpoint: http://otel-collector-headless:4318
  sampler:
    type: parentbased_always_on
  java:
    image: ghcr.io/open-telemetry/opentelemetry-operator/autoinstrumentation-java:2.19.0
```

- Install the instrumentation:

```sh
kubectl apply -f observability/otel-instrumentation.yaml
```

- Restart all deployments to take the instrumentation into account

```sh
kubectl rollout restart deploy order
kubectl rollout restart deploy product
kubectl rollout restart deploy stock
kubectl rollout restart deploy shopping-cart
```

- Relaunch order simulation

```sh
kubectl apply -f ./traffic-simulation/traffic-simulation-pod.yaml
```

- Go to the [signoz dashboard](http://signoz.k3s.local) and try to pinpoint the issue

### Add custom spans

### Add custom metrics

Foolowing the incident, we want to avoid being alerted by our customers. We'd like to track in our "order" microservice how many orders are created by second.

Tip: use `MeterRegistry` that you can inject with Spring Boot.
Add this dependency to the "order" microservice:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-otlp</artifactId>
</dependency>
```

Once the code is ready, push it under your repository. Update the deplyoment in "microservices/order/infra/values.yaml" to target your repository.
Check in Signoz that the metric is working. You can even try to create a dashbord to follow this metric and thus be alerted depending on different tresholds.

### Bonus => Filter traces
