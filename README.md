# Hands On - OpenTelemetry

## MacOS

## Linux

## Windows avec PowerShell

### Installer winget

[Doc Microsoft](https://learn.microsoft.com/fr-fr/windows/package-manager/winget/)

### Installer Docker Desktop

Docker Desktop

[Doc K3D au cas où](https://k3d.io/stable/#other-installers)

### Installer mise

[Doc démarrage](https://mise.jdx.dev/getting-started.html)

Depuis le dossier où a été cloné le projet

```powershell
winget install jdx.mise
Invoke-Item $profile
# Coller le contenu pour [activer mise](https://mise.jdx.dev/getting-started.html#activate-mise)
mise install
```

Tout est stocké dans `%LocalAppData%\mise`.

### Installer Headlamp

[Site officiel](https://headlamp.dev/)

```powershell
winget install headlamp
```

Basculer en langue anglaise dans les paramètres

## Début du TP

### Configuration des clusters

```powershell
k3d cluster create otel-hands-on --agents 2 # on crée 2 noeuds dans le cluster pour "avoir un peu puissance"
kubectl create namespace microservices
kubectl create namespace observability
```
