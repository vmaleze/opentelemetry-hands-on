# Hands On - OpenTelemetry

## Repository content

- microservices: 4 Spring Boot applications
- observability: configuration of the observability stack
- presentation-slides: the presentation in format [Slidev](https://sli.dev/)
- signoz: configuration of Signoz
- traffic-simulation: script to simulate the traffic on the microservices

## MacOS

## Linux

## Windows with PowerShell

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

## TP 1: start

### Cluster configuration

```sh
k3d cluster create otel-hands-on --agents 2 --port "80:80@loadbalancer" --port "443:443@loadbalancer"
kubectl create namespace microservices
kubens microservices

### Kafka
kubectl apply -n microservices -f microservices/infra/kafka-deployment.yaml

### Signoz
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

Check in Headlamp that everything is fine.

Modify your hosts file with the DNS entries for each microservice:

- On MacOS or Linux, open "/etc/hosts" with any text editor, **as "sudo"**.
- On Windows, open "%SystemRoot%\System32\drivers\etc\hosts" with any text editor, **launched as an administrator**.
  - Either [enable sudo](https://learn.microsoft.com/en-us/windows/advanced-settings/sudo/#how-to-enable-sudo-for-windows), and then run `sudo notepad`
  - Or right click on Notepad and choose "Run as admnistrator"
- Add the following line to "hosts":

```txt
127.0.0.1 order.k3s.local product.k3s.local shopping-cart.k3s.local stock.k3s.local signoz.k3s.local
```

### Launch order simulation

```sh
kubectl apply -f ./traffic-simulation-pod.yaml
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
spec:
  mode: daemonset # Choose how to deploy the collector, cf https://github.com/open-telemetry/opentelemetry-operator/blob/main/README.md#deployment-modes
  image: otel/opentelemetry-collector-contrib:0.131.0
  config:
    receivers: # How to recieve Open Telemetry data.
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
