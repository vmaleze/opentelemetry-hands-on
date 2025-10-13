[< home](../README.md)

# Lab 3: Add some business value

> [!NOTE]  
> This part requires the usage of your forked repository.  
> When you push on your repo, a pipeline will automatically build the service. Once built, you need to update the `<service>/infra/values.yaml` to target your repository `ghcr.io/<github_account>/opentelemetry-hands-on/<service>`

## Add custom metrics

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

## Add custom spans

Can you add the shopping cart id into the traces to easily find what cart is causing trouble ?

Tips: You need to add the following dependency and then work on the current span => [Spring Boot Starter](https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/annotations/)

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

## Next

[Lab 4 (Bonus): Filter traces](tp4.md)
