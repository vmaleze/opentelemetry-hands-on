# Hands On - OpenTelemetry

## Abstract

You can start by reading [the abstract](labs/abstract.md) (in French) of this hands on.

## Prerequisites

- OS session with administrator permissions
- IDE like IntelliJ or Visual Studio Code, supporting Java
- PowerShell 7 if you're using Windows: get it from the [documentation](https://learn.microsoft.com/en-us/powershell/scripting/install/installing-powershell-on-windows?view=powershell-7.5) using WinGet
- Bash (or zsh of fish) if you're using MacOS or Linux
- Docker
- Git and a [GitHub](https://github.com/) account
- [mise](https://mise.jdx.dev/getting-started.html): don't forget to properly [add it to your shell](https://mise.jdx.dev/installing-mise.html#shells) with `mise activate <shell>` after the installation
- [Headlamp](https://headlamp.dev/), unless you master the command line around `kubectl`

## Repository content

- microservices: 4 Spring Boot applications
- observability: configuration of the observability stack
- presentation-slides: the presentation in format [Slidev](https://sli.dev/)
- signoz: configuration of SigNoz
- traffic-simulation: script to simulate the traffic on the microservices

The presentation slides are available on [GitHub Pages](https://vmaleze.github.io/opentelemetry-hands-on/).

## Setup

1. **Fork** this repository (you will need your own GitHub repo for later stages of this hands-on) and clone it locally.

1. Run `mise install` at the root of the repository.

1. Depending on your OS, run the according script to setup the environment (`setup.sh` or `setup.ps1`) .
   The script check an install pre-requisites for the TP.  
   It also setup a local k3d cluster with the necessary config.

1. Configure your hosts

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

### Restart

If you need to relaunch the cluster (after exiting down Docker for example):

```sh
k3d cluster start otel-hands-on
```

## Cleaning

If you'd like to clean your computer after the lab:

- Remove the line added to your hosts file
- Uninstall Headlamp
- Uninstall mise: `mise implode` (more detail on the [official documentation](https://mise.jdx.dev/installing-mise.html#uninstalling))
- Remove the cluster and its content:

```sh
k3d cluster delete otel-hands-on # should remove everything, except the images

docker image ls # list all Docker images
docker container ls # list all Docker containers
docker system prune # remove all unused data
docker image rm <IMAGE ID> # remove a specific image
docker container rm <CONTAINER ID> # remove a specific container
```

## Labs

- [Lab 1: Work without observability](labs/tp1.md)
- [Lab 2: Let's add some observability](labs/tp2.md)
- [Lab 3: Add some business value](labs/tp3.md)
- [Lab 4 (Bonus): Filter traces](labs/tp4.md)
