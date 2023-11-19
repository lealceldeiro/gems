# Quick Start Kubernetes

Main notes taken from the book [Quick Start Kubernetes](https://leanpub.com/quickstartkubernetes)

[Official Documentation](https://kubernetes.io/docs/)

Kubernetes is an orchestrator of cloud-native microservices applications.

A cloud-native app must:

- Scale on demand
- Self-heal
- Support rolling updates
- Run anywhere that has Kubernetes

K8s sometimes is referred to as the OS of the cloud.

A Kubernetes cluster is one or more machines with Kubernetes installed.

We normally refer to machines in a Kubernetes cluster as nodes.

Node types:

- Control plane nodes
- Worker nodes

Some services running on worker nodes:

- Kubelet: main Kubernetes agent
- Container runtime: starts and stops containers

Most of the day-to-day management of a Kubernetes cluster can be done using the Kubernetes command line tool: `kubectl`.

Kubernetes runs containers inside of Pods.

Kubernetes uses a dedicated _Service_ object to provide network connectivity to apps running in _Pods_.

Kubernetes uses a dedicated _Deployments_ object to provide self-healing. It also enables scaling and rolling updates.

The container provides the OS and other app dependencies; the Pod provides metadata and other constructs for the
container to run on Kubernetes; the Deployment provides cloud-native features, including self-healing.

The _Deployment object_ is the YAML configuration that defines the Pod and container. It also defines things such as
how many Pod replicas to deploy.

The _Deployment controller_ is a process running on the control plane that is constantly monitoring the cluster making
sure all Deployment objects are running as they are supposed to.

`kubectl get pods -o wide`: lists all Pods on the cluster and the worker node each Pod is running on
