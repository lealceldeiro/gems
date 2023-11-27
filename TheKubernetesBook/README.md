# The Kubernetes Book

Main notes taken from the book [The Kubernetes Book](https://leanpub.com/thekubernetesbook)

Examples of higher-level controllers include `Deployment`s, `DaemonSet`s, and `StatefulSet`s.

Every node runs a service called the _kubelet_ that registers it with the cluster and communicates with the API server.

----

Pods are the atomic unit of scheduling in Kubernetes.

Single-container Pods are the simplest. However, multi-container Pods are ideal for co-locating tightly coupled
workloads and are fundamental to service meshes.

Pods get scheduled on nodes (host physical servers, VMs, cloud instances) -- a single Pod can't be scheduled to span
multiple nodes.

Pods are defined declaratively in manifest files which are posted to the API server.

Pods are almost always deployed via higher-level controllers.

Pods augment containers:

- Labels and annotations
- Restart policies
- Probes (startup probes, readiness probes, liveness probes, and potentially more)
- Affinity and anti-affinity rules
- Termination control
- Security policies
- Resource requests and limits

Some Pod properties:

- Labels: group Pods and associate them with other objects.
- Annotations: allows adding experimental features and integrations with 3rd-party tools and services.
- Probes: allows testing the health and status of Pods, enabling advanced scheduling, updates, and more.
- Affinity and anti-affinity rules: give control over where Pods run.
- Termination control: allows to gracefully terminate Pods and the applications they run.
- Security policies: allows to enforce security features.
- Resource requests and limits: allows to specify minimum and maximum values for things like CPU, memory and disk IO.

Introspect pod:

- `kubectl get pods <pod> -o yaml`
- `kubectl describe pods <pod>`
- `kubectl logs <pod>` (example: `kubectl logs multipod --container syncer`)

----

- `kubectl get namespaces`: list namespaces
- `kubectl describe ns <namespace>`: inspect a namespace
- `kubectl get svc --namespace <namespace>`: list services by filtering by a specific namespace
- `kubectl get svc --all-namespaces`: list services from all namespaces
- `kubectl create ns <namespace>`: create a namespace
- `kubectl delete ns <namespace>`: delete a namespace
- `kubectl config set-context --current --namespace <namespace>`: configure `kubectl` to run all future commands
  against a specific namespace

The _Deployment_ controller is specifically designed for stateless apps.

- `kubectl get deploy <deployment>` and `kubectl describe deploy <deployment>`: see details of a deployment and the
  R`eplicaSet`s.
- `kubectl get rs` and `kubectl get rs <replicaset>`: get `ReplicaSet`s info
- `kubectl rollout pause deploy <deployment>`: pause a rollout
- `kubectl rollout resume deploy <deployment>`: resume a rollout
- `kubectl rollout undo deployment <deployment> --to-revision=<revision-number>`: rollback a deployment

----

- `kubectl describe svc <service-name>`: describe a service

where

- `Selector` is the list of labels the Service looks for when building its list of Pods to send traffic to
- `IP` is the permanent internal ClusterIP (VIP) of the Service
- `Port` is the port the Service listens on inside the cluster
- `TargetPort` is the port the application is listening on
- `NodePort` is the cluster-wide port that can be used for external access
- `Endpoints` is the dynamic list of healthy Pod IPs that match the selector

`kubectl get endpointslices`: get the service endpoint slices

----

Ingress is a way to expose multiple applications and Kubernetes Services via a single cloud load-balancer.

When running a service mesh an Ingress may not be needed.

Most Kubernetes clusters require us to install an Ingress controller.

An Ingress controller governs how incoming traffic is routed to applications and Services. It supports host-based and
path-based HTTP routing.

----

Two major components to service discovery:

- Registration: process of an application posting its connection details to a service registry so other apps can find it
  and consume it
- Discovery

Kubernetes uses its internal DNS as a service registry.

All Kubernetes Services automatically register their details with DNS, and all containers are pre-configured to use the
cluster DNS for service discovery.

The cluster DNS resolves Service names to ClusterIPs. These are stable virtual IPs on a special network called the
_service network_. Although there are no routes to this network, the `kube-proxy` configures all cluster nodes to
redirect traffic destined for the service network to Pod IPs on the Pod network.

----

Persistent volume subsystem: Kubernetes storage subsystem.

_Container Storage Interface (CSI)_ is an open standard aimed at providing a clean storage interface for container
orchestrators such as Kubernetes.

- Persistent Volumes (PV): mapped to external storage assets
- Persistent Volume Claims (PVC): authorizes applications (Pods) to use PVs
- Storage Classes (SC): dynamically creates physical back-end storage resources that get automatically mapped to
  Persistent Volumes (PV)

Each provider (a.k.a. provisioner) needs a CSI plugin to expose their storage assets to Kubernetes. The plugin
usually runs as a set of Pods in the `kube-system` Namespace.

Access modes for volumes:

- ReadWriteOnce (RWO): the PV can only be bound as R/W by a single PVC
- ReadWriteMany (RWM): the PV can be bound as R/W by multiple PVCs
- ReadOnlyMany (ROM): the PV can be bound as R/O by multiple PVCs

----

ConfigMaps and Secrets are the Kubernetes native way of decoupling applications and config data.

They’re both first-class object in the Kubernetes API and can be created and manipulated with `kubectl apply`,
`kubectl get`, and `kubectl describe` commands.

ConfigMaps are designed for application configuration parameters and even entire configuration files, whereas Secrets
are designed for sensitive configuration data.

Both can be injected into containers at run-time via various constructs, with volumes being the preferred method,
as they allow updates to eventually be reflected in running containers.

Secrets are not encrypted in the cluster store, not encrypted in-flight on the network, and not encrypted when surfaced
in a container.

----

`StatefulSet`s are for applications that need Pods to be predictable and long-lived.

`StatefulSet`s guarantee:

- Predictable and persistent Pod names
- Predictable and persistent DNS hostnames
- Predictable and persistent volume bindings

Example: failed Pods managed by a _StatefulSet_ will be replaced by new Pods with the exact same Pod name, the exact
same DNS hostname, and the exact same volumes. This is true even if the replacement Pod is started on a different
cluster node. The same is not true of Pods managed by a _Deployment_.

Usually, rollbacks in replicas managed by `StatefulSet`s require manual attention.

Each Pod replica spawned by a StatefulSet gets a predictable and persistent name, DNS hostname, and unique set of
volumes. These stay with the Pod for its entire lifecycle, including failures, restarts, scaling, and other scheduling
operations. In fact, StatefulSet Pod names are integral to scaling operations and connecting to storage volumes.
