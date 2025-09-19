[< home](../README.md)

# Lab 1: Work without observability

## Launch some trafic on the website

```sh
kubectl apply -f ./traffic-simulation/traffic-simulation-pod.yaml
```

The traffic simulation stops automatically after 10 minutes.

## Find the bug

There's a bug in the supply chain order: someone came to tell us that the order flow is suprislingly low and would like us to investigate. Can you find what's wrong?

Tip: check logs in Headlamp or via `kubectl get logs deployments/<deployment-name> -f`.

## Next

[Lab 2: Let's add some observability](tp2.md)
