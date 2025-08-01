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
k3d cluster create otel-hands-on --agents 2 --port "80:80@loadbalancer" --port "443:443@loadbalancer"
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
helm dependency up ./microservices/order/infra && helm upgrade --install order ./microservices/order/infra
helm dependency up ./microservices/product/infra && helm upgrade --install product ./microservices/product/infra
helm dependency up ./microservices/shopping-cart/infra && helm upgrade --install shopping-cart ./microservices/shopping-cart/infra
helm dependency up ./microservices/stock/infra && helm upgrade --install stock ./microservices/stock/infra
```

Check in Headlamp that everything is fine.

Modify your hosts file with the DNS entries for each microservice:

- On MacOS or Linux, open "/etc/hosts" with any text editor, **as "sudo"**.
- On Windows, open "%SystemRoot%\System32\drivers\etc\hosts" with any text editor, **launched as an administrator**.
- Add the following line to "hosts":

```txt
127.0.0.1 order.k3s.local product.k3s.local shopping-cart.k3s.local stock.k3s.local
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
