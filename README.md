# Hands On - OpenTelemetry

## Abstract

You can start by reading [the abstract](labs/abstract.md) (in French) of this hands on.

## Prerequisites

- OS session with administrator permissions
- IDE like IntelliJ or Visual Studio Code
- PowerShell 7 or Bash
- Docker
- Git and a [GitHub](https://github.com/) account
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

## Labs

- [Lab 1: Work without observability](labs/tp1.md)
- [Lab 2: Let's add some observability](labs/tp2.md)
- [Lab 3: Add some business value](labs/tp3.md)
- [Lab 4 (Bonus): Filter traces](labs/tp4.md)
