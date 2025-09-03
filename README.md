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

## Setup

1. Fork this repository (you will need your own github repo for later stages of this hands-on) and clone it locally.

1. Depending on your OS, run the according script to setup the environment. (`setup.sh` or `setup.ps1`)  
The script check an install pre-requisites for the TP.  
It also setup a local k3d cluster with the necessary config.

1. Configure your host

As we are all working locally, and to make this hands-on easier for all, we need to all use the same urls.
To do so, modify your hosts file with the DNS entries for each microservice:

- On MacOS or Linux, open "/etc/hosts" with any text editor, **as "sudo"**.
- On Windows, open "%SystemRoot%\System32\drivers\etc\hosts" with any text editor, **launched as an administrator**.
  - Either [enable sudo](https://learn.microsoft.com/en-us/windows/advanced-settings/sudo/#how-to-enable-sudo-for-windows), and then run `sudo notepad`
  - Or right click on Notepad and choose "Run as admnistrator"
- Add the following line to "hosts":

```txt
127.0.0.1 order.k3d.local product.k3d.local shopping-cart.k3d.local stock.k3d.local signoz.k3d.local
```

Check in Headlamp that everything is fine.

You can reach the APIs documentation at the following URLs: [order](http://order.k3d.local/swagger-ui/index.html), [product](http://product.k3d.local/swagger-ui/index.html), [shopping-cart](http://shopping-cart.k3d.local/swagger-ui/index.html), [stock](http://stock.k3d.local/swagger-ui/index.html).  

## TP 1: Launch some trafic on the website

```sh
kubectl apply -f ./traffic-simulation/traffic-simulation-pod.yaml
```

The traffic simulation stops automatically after 10 minutes.

### Find a bug

There's a bug in the supply chain order: someone came to tell us that the order flow is suprislingly low and would like us to investigate. Can you find what's wrong?

Tip: check logs in Headlamp or via `kubectl get logs deployments/<deployment-name> -f`.

## TP 2: Let's add some observability

To monitor our microservices, we will use [opentelemetry](https://opentelemetry.io/) and [Signoz](https://signoz.io/).

### Signoz

SigNoz is an open-source Datadog or New Relic alternative. Get APM, logs, traces, metrics, exceptions, & alerts in a single tool.

```sh
## Install Signoz
helm install --wait signoz signoz/signoz --namespace observability --create-namespace -f signoz/values.yaml

## Add an ingress to access signoz on http://signoz.k3d.local
kubectl apply -f signoz/ingress.yaml
```

### OpenTelemetry

As we are in kubernetes, we will install the following components:  
* [OpenTelemetry operator](https://opentelemetry.io/docs/platforms/kubernetes/operator/) => An implementation of a Kubernetes Operator, that manages collectors and auto-instrumentation of the workload using OpenTelemetry instrumentation libraries.
* [OpenTelemetry Collector](https://opentelemetry.io/docs/collector/) => Vendor-agnostic way to receive, process and export telemetry data.
* [OpenTelemetry Auto-Instrumentation](https://opentelemetry.io/docs/platforms/kubernetes/operator/automatic/) => An implementation of auto-instrumentation using the OpenTelemetry Operator.  
The OpenTelemetry Operator supports injecting and configuring auto-instrumentation libraries for .NET, **Java**, Node.js, Python, and Go services.

#### OpenTelemetry operator

```sh
## Create a separate namespace to avoid mixing everything
kubectl create ns opentelemetry-operator-system

## Installing cert manager required by the opentelemetry operator. 
## We wait for all pods to be ready to avoid any issues while installing the operator after.
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.18.2/cert-manager.yaml && kubectl wait -n cert-manager --for=condition=Ready pods --all --timeout=300s

## Applying the opentelemetry-operator manifest (1).
kubectl -n opentelemetry-operator-system apply -f https://github.com/open-telemetry/opentelemetry-operator/releases/latest/download/opentelemetry-operator.yaml
```

(1). We prefer using the yaml manifest to always be on the latest version. A [helm chart](https://github.com/open-telemetry/opentelemetry-helm-charts/tree/main/charts/opentelemetry-operator) exists for the operator, but it is generally not updated fast enough to follow the latest changes.

#### OpenTelemetry Collector

- Create an "observability" directory.
- Create an "otel-collector.yaml" file with:

```yaml
apiVersion: opentelemetry.io/v1beta1
kind: OpenTelemetryCollector
metadata:
  name: otel
  namespace: microservices
spec:
  mode: daemonset # Specifies how to deploy the collector, cf https://github.com/open-telemetry/opentelemetry-operator/blob/main/README.md#deployment-modes
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

### Java agent

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

- Go to the [signoz dashboard](http://signoz.k3d.local) and try to pinpoint the issue

## TP3: Add some business value

### Add custom spans

We just saw that there was an issue with the shopping cart id. Can you add this id into the traces to easily find what cart is causing trouble ?

Tips: You need to add the following dependency and then work on the current span => https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/annotations/
```xml
<dependency>
    <groupId>io.opentelemetry.instrumentation</groupId>
    <artifactId>opentelemetry-spring-boot-starter</artifactId>
</dependency>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.opentelemetry.instrumentation</groupId>
            <artifactId>opentelemetry-instrumentation-bom</artifactId>
            <version>2.19.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Add custom metrics

Following the incident, we want to avoid being alerted by our customers. We'd like to track in our "order" microservice how many orders are created by second.

Tips: Use `MeterRegistry` that you can inject with Spring Boot.
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
