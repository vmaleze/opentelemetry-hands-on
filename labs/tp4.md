[< home](../README.md)

# Lab 4 (Bonus) => Sampling

Learn about sampling and the different sampling options available in OpenTelemetry.

With traces, you can observe requests as they move from one service to another in a distributed system. Tracing is highly practical for both high-level and in-depth analysis of systems.

However, if the large majority of your requests are successful and finish with acceptable latency and no errors, you do not need 100% of your traces to meaningfully observe your applications and systems. You just need the right sampling.

![Sampling](assets/sampling-diagram.svg "sampling-diagram")

So far we have used [head-sampling](https://opentelemetry.io/docs/concepts/sampling/#head-sampling) and gathered all traces.

```yaml
# otel-instrumentation.yaml
sampler:
  type: parentbased_always_on # This config samples all traces.
```

## Let's filter our traces

To effectively filter traces, we will be using [tail-sampling](https://opentelemetry.io/docs/concepts/sampling/#tail-sampling).

### The challenge

Tail sampling requires to have all traces in one place to take a decision. As we are in a distributed environment, we need to have this in mind.

1. Setup the sampler collector

- Create an `otel-sampler-collector.yaml` file with:

```yaml
apiVersion: opentelemetry.io/v1beta1
kind: OpenTelemetryCollector
metadata:
  name: otel-sampler
spec:
  mode: daemonset
  image: otel/opentelemetry-collector-contrib:0.133.0
  config:
    receivers:
      otlp:
        protocols:
          grpc: {}
          http: {}

    processors:
      tail_sampling:
        decision_wait: 10s
        num_traces: 500
        policies: [
            {
              # Do not sample calls to actuator and swagger
              name: actuator-swagger-routes,
              type: string_attribute,
              string_attribute:
                {
                  key: url.path,
                  values: [\/actuator.*, \/swagger-ui.*],
                  enabled_regex_matching: true,
                  invert_match: true,
                },
            },
            {
              # Always sample errors
              name: errors-policy,
              type: status_code,
              status_code: { status_codes: [ERROR] },
            },
            {
              # Sample 10% of successful requests
              name: randomized-policy,
              type: probabilistic,
              probabilistic: { sampling_percentage: 10 },
            },
          ]

    exporters:
      otlp/signoz:
        endpoint: signoz-otel-collector.observability:4317
        tls:
          insecure: true

    service:
      pipelines:
        traces:
          receivers: [otlp]
          processors: [tail_sampling]
          exporters: [otlp/signoz]
```

- Apply it to the cluster

```bash
kubectl apply -f observability/otel-sampler-collector.yaml
```

2. Edit the `otel-collector.yaml` to send data to the sampler

```yaml
spec:
  ...
  config:
    ...
    exporters: # Where to export OpenTelemetry data.
      otlp/signoz:
        ...
      loadbalancing: # Introducing the loadbalancer exporter that can route traces to the same collector based on a routing_key
        routing_key: "traceID"
        protocol:
          otlp:
            tls:
              insecure: true
        resolver:
          dns:
            hostname: otel-sampler-collector-headless

    service: # Configure each pipeline of Open Telemetry data.
      pipelines:
        logs:
          ...
        traces:
          receivers: [otlp]
          exporters: [loadbalancing] # Changing the exporter to send traces to the otel-sampler-collector
        metrics:
          ...
```

- Apply it to the cluster

```bash
kubectl apply -f observability/otel-collector.yaml
```

And that's it. You should now only see filtered traces in Signoz.
