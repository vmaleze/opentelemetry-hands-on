[< home](../README.md)

# Lab 2: Let's add some observability

To monitor our microservices, we will use [opentelemetry](https://opentelemetry.io/) and [Signoz](https://signoz.io/).

## Signoz

SigNoz is an open-source Datadog or New Relic alternative. Get APM, logs, traces, metrics, exceptions, & alerts in a single tool.

```sh
## Install Signoz (takes quite some time)
helm install signoz signoz/signoz --namespace observability --create-namespace -f signoz/values.yaml

## Add an ingress to access signoz on http://signoz.k3d.local
kubectl apply -f signoz/ingress.yaml
```

You can check when Signoz is up and running by looking at its pods state with Headlamp. Don't forget to switch to the "observabiliy" namespace to find them!

## OpenTelemetry

OpenTelemetry is a collection of APIs, SDKs, and tools. Use it to instrument, generate, collect, and export telemetry data (metrics, logs, and traces) to help you analyze your software’s performance and behavior.

As we are in kubernetes, we will install the following components:

- [OpenTelemetry operator](https://opentelemetry.io/docs/platforms/kubernetes/operator/) => An implementation of a Kubernetes Operator, that manages collectors and auto-instrumentation of the workload using OpenTelemetry instrumentation libraries.
- [OpenTelemetry Collector](https://opentelemetry.io/docs/collector/) => Vendor-agnostic way to receive, process and export telemetry data.
- [OpenTelemetry Auto-Instrumentation](https://opentelemetry.io/docs/platforms/kubernetes/operator/automatic/) => An implementation of auto-instrumentation using the OpenTelemetry Operator.  
  The OpenTelemetry Operator supports injecting and configuring auto-instrumentation libraries for .NET, **Java**, Node.js, Python, and Go services.

### OpenTelemetry operator

```sh
## Create a separate namespace to avoid mixing everything
kubectl create ns opentelemetry-operator-system

## Installing cert manager required by the opentelemetry operator.
## We wait for all pods to be ready to avoid any issues while installing the operator after.
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.18.2/cert-manager.yaml && kubectl wait -n cert-manager --for=condition=Ready pods --all --timeout=300s

## Applying the opentelemetry-operator manifest (1).
kubectl -n opentelemetry-operator-system apply -f https://github.com/open-telemetry/opentelemetry-operator/releases/latest/download/opentelemetry-operator.yaml
```

(1). We prefer to use the yaml manifest to always be on the latest version. A [helm chart](https://github.com/open-telemetry/opentelemetry-helm-charts/tree/main/charts/opentelemetry-operator) exists for the operator, but it is generally not updated fast enough to follow the latest changes.

### OpenTelemetry Collector

- Understanding the collector
  ![Collector](assets/otel-collector.png "otel-collector")

- Create an `observability` directory.
- Create an `otel-collector.yaml` file with:

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

- Create an `otel-instrumentation.yaml` file with:

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
    type: parentbased_always_on # see tp4 for more information
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

> [!NOTE]  
> All deployments are using a helm chart that already takes into account the injection of the java agent.  
> Feel free to check the helm chart to know how it's done => [here](https://github.com/vmaleze/opentelemetry-hands-on/blob/e492bdfc362b5eb8b4900102f4ac7c147277d7e7/microservices/infra/microservices-base-chart/templates/deployment.yaml#L21)

- Relaunch order simulation

```sh
kubectl delete pod traffic-simulation-pod
kubectl apply -f ./traffic-simulation/traffic-simulation-pod.yaml
```

## Find the bug

- Go to the [signoz dashboard](http://signoz.k3d.local) and try to pinpoint the issue.

## Next

[Lab 3: Add some business value](tp3.md)
