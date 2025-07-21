# Hands On - OpenTelemetry

## Repository content

- microservices : 4 Spring Boot applications
- presentation-slides : the presentation in format [Slidev](https://sli.dev/)

## MacOS

## Linux

## Windows with PowerShell

### Install winget

[Microsoft documentatiopn](https://learn.microsoft.com/fr-fr/windows/package-manager/winget/)

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

## Début du TP

### Configuration des clusters

```powershell
k3d cluster create otel-hands-on --agents 2 # on crée 2 noeuds dans le cluster pour "avoir un peu puissance"
kubectl create namespace microservices
kubectl create namespace observability
```

Vérifier que tout va bien dans Headlamp.
