# Hands On - OpenTelemetry

## Repository content

- microservices : 4 Spring Boot applications
- presentation-slides : the presentation in format [Slidev](https://sli.dev/)
- traffic-simulation : script to simulate the traffic on the microservices

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
k3d cluster create otel-hands-on --agents 2 # We create 2 nodesin the cluster to have "some power"
kubectl create namespace microservices
kubens microservices
kubectl apply -n microservices -f microservices/infra/kafka-deployment.yaml
```

Check in Headlamp that everything is fine.

If you need to relaunch the cluster:

```sh
k3d cluster start otel-hands-on
```

### Microservices installation

```sh
helm dependency up ./microservices/order/infra && helm install order ./microservices/order/infra
helm dependency up ./microservices/product/infra && helm install product ./microservices/product/infra
helm dependency up ./microservices/shopping-cart/infra && helm install shopping-cart ./microservices/shopping-cart/infra
helm dependency up ./microservices/stock/infra && helm install stock ./microservices/stock/infra
```

### Launch order simulation

```sh
kubectl apply -f ./traffic-simulation/pod.yaml
```

### Find a bug

There's a bug in the supply chain order: someone came to tell us that the order flow is suprislingly low and likes us to investigate. Can you find what's wrong?

Tip: check logs in Headlamp or via `kubectl get logs deployments/<deployment-name> -f`.

## TP 2: install the observability stack
