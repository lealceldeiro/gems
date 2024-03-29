# Docker Deep Dive

Main notes taken from the book [Docker Deep Dive](https://www.amazon.com/Docker-Deep-Dive-Nigel-Poulton/dp/1521822808)

## Part 1

### Chapter 4

When running a container in iterative mode (`-it`) the shell prompt is switched into the terminal of the container. To
exit the container without terminating it, press **Ctrl**-**PQ**.

You can attach your shell to the terminal of a running container with the `docker container exec` command.

## Part 2

### Chapter 5: The Docker Engine

The Docker engine is modular in design and based heavily on open-standards from the OCI.

The Docker *daemon* implements the Docker API which is currently a rich, versioned, HTTP API that has developed
alongside the rest of the Docker project.

Container execution is handled by *containerd*.

By default, Docker uses `runc` as its default container runtime.

### Chapter 6: Images

Docker provides the `--filter` flag to filter the list of
images returned by `docker image ls`.

Currently, the following filters are  supported:

- `dangling`: Accepts `true` or `false`, and returns only dangling images (`true`), or non-dangling images (`false`).

- `before`: Requires an image name or ID as argument, and returns all images created before it.

- `since`: Same as above, but returns images created after the specified image.

- `label`: Filters images based on the presence of a label or label and value. The `docker image ls` command does not
display labels in its output.

For all other filtering you can use `reference` (i.e.: `docker image ls --filter=reference="*:latest"`).

You can also use the `--format` flag to format output using *Go* templates. i.e.:
```shell
docker image ls --format "{{.Repository}}: {{.Tag}}: {{.Size}}"
alpine: latest: 5.61MB
redis: latest: 98.3MB
busybox: latest: 1.22MB
```

You can delete images on a system with the `docker image prune` command. By adding `-a`, all images will be deleted.

#### Commands to work with images

- `docker image pull`: download images
- `docker image ls`: list all the images stored in Docker host’s local image cache. The `--digests` flag displays 
the SHA256 digests.
- `docker image inspect`: show the details of an image -- layer data and metadata
- `docker manifest inspect`: show the manifest list of any image stored on Docker Hub
- `docker buildx` is a Docker CLI plugin that extends the Docker CLI to support multi-arch builds
- `docker image rm`: delete images

### Chapter 7: Containers

- `docker container run` is the command used to start new containers.
- `Ctrl-PQ` will detach your shell from the terminal of a container and leave the container running (UP) in the
background.
- `docker container ls` lists all containers in the running (UP) state. If you add the -a flag you will also see
containers in the stopped (Exited) state.
- `docker container exec` runs a new process inside a running container. It’s useful for attaching the shell of your
Docker host to a terminal inside a running container.
- `docker container stop` will stop a running container and put it in the Exited (0) state.
- `docker container start` will restart a stopped (Exited) container.
- `docker container rm` will delete a stopped container.
- `docker container inspect` will show you detailed configuration and runtime information about a container.

### Chapter 11: Docker networking

The Container Network Model (CNM) is the master design document for Docker networking and defines the three major
constructs that are used to build Docker networks — sandboxes, endpoints, and networks.

`libnetwork` is the open-source library, written in Go, that implements the CNM. It’s used by Docker and is where all
the core Docker networking code lives. It also provides Docker’s network control plane and management plane.

The simplest type of Docker network is the single-host bridge network.

`docker network ls`: list networks
`docker network inspect <name>`: inspect network with name `<name>`. i.e.: `docker network inspect bridge`

Docker networks built with the `bridge` driver on Linux hosts are based on the _linux bridge_ technology that has
existed in the Linux kernel for nearly 20 years.

The default bridge network on Linux does not support name resolution via the Docker DNS service. All other user-defined
bridge networks do.

Overlay networks are multi-host. They allow a single network to span multiple hosts so that containers on different
hosts can communicate directly. They’re ideal for container-to-container communication, including container-only
applications, and they scale well.

To check the Docker daemon logs and the container logs, they can be found:

- On Windows systems, the daemon logs are stored under `~AppData\Local\Docker`, and can be viewed in the Windows Event
Viewer
- On Linux, it depends on what init system it's using. If it's systemd, the logs will go to journald and can be viewed
with the `journalctl -u docker.service` command. If it's not `systemd` the following locations should be checked:
  * Ubuntu systems running upstart: `/var/log/upstart/docker.log`
  * RHEL-based systems: `/var/log/messages`
  * Debian: `/var/log/daemon.log`

Logs from standalone containers can be viewed with the `docker container logs` command.

Swarm service logs can be viewed with the `docker service logs` command.

When using other logging drivers the logs  can be viewed using the 3-rd party platform’s native tools.

_Service discovery_ allows all containers and Swarm services to locate each other by name. The only requirement is that
they be on the same network (service discovery is network-scoped).

- `docker network create`: Creates new Docker networks
- `docker network prune`: Deletes all unused networks on a Docker host
- `docker network rm`: Deletes specific networks on a Docker host

### 12: Docker overlay networking

- `docker service ps`: list docker services

### 13: Volumes and persistent data

Every Docker container is created by adding a thin read-write layer on top of the read-only image it’s based on.

The writable container layer exists in the filesystem of the Docker host, and it can be called various names. These
include _local storage_, _ephemeral storage_, and _graphdriver storage_.

It’s typically located on the Docker host in these locations:

- Linux Docker hosts: `/var/lib/docker/<storage-driver>/...`
- Windows Docker hosts: `C:\ProgramData\Docker\windowsfilter\...`

When running Docker in production on Linux, you’ll need to make sure you match the right storage driver with the Linux
distribution on the Docker host. The following list can be used as a guide:

- _Red Hat Enterprise Linux_: Use the `overlay2` driver with modern versions of RHEL running Docker 17.06 or higher. Use
the `devicemapper` driver with older versions. This applies to Oracle Linux and other Red Hat related upstream and
downstream distros.
- _Ubuntu_: Use the `overlay2` or `aufs` drivers. If you’re using a Linux 4.x kernel or higher you should go with
`overlay2`.
- _SUSE Linux Enterprise Server_: Use the `btrfs` storage driver.
- _Windows_: Windows only has one driver, and it is configured by default.

Volumes are the recommended way to persist data in containers. There are three major reasons for this:

- Volumes are independent objects that are not tied to the lifecycle of a container
- Volumes can be mapped to specialized external storage systems
- Volumes enable multiple containers on different Docker hosts to access and share the same data

All volumes created with the local driver get their own directory under `/var/lib/docker/volumes` on Linux, and
`C:\ProgramData\Docker\volumes` on Windows.

A major concern with any configuration that shares a single volume among multiple containers is data corruption.

#### Commands

- `docker volume create`: create a new volumes. By default, volumes are created with the local driver, but the -d flag
can be used to specify a different driver.
- `docker volume ls`: list all volumes on the local Docker host.
- `docker volume inspect`: show detailed volume information.
- `docker volume prune`: delete all volumes that are not in use by a container or service replica. Use with caution!
- `docker volume rm`: delete specific volumes that are not in use.
- `docker plugin install`: install new volume plugins from Docker Hub.
- `docker plugin ls`: list all plugins installed on a Docker host.

### 14: Deploying apps with Docker Stacks

Stacks are the native Docker solution for deploying and managing cloud-native microservices applications with multiple
services.

They're baked into the Docker engine, and offer a simple declarative interface for deploying and managing the entire
lifecycle of an application.

When Docker stops a container, it issues a `SIGTERM` to the application process with PID 1 inside the container. The
application then has a 10-second grace period to perform any clean-up operations. If it doesn't handle the signal, it
will be forcibly terminated after 10 seconds with a `SIGKILL`. The `stop_grace_period` property overrides this 10-second
grace period.

- `docker stack deploy` is the command for deploying and updating stacks of services defined in a stack file (usually
called `docker-stack.yml`).
- `docker stack ls` lists all stacks on the Swarm, including how many services they have.
- `docker stack ps` gives detailed information about a deployed stack. It accepts the name of the stack as its main
argument, lists which node each replica is running on, and shows _desired state_ and _current state_.
- `docker stack rm` deletes a stack from the Swarm. It does not ask for confirmation before deleting the stack.

### 15: Security in Docker

**Docker Swarm Mode** is secure by default.

**Docker Content Trust (DCT)** lets you sign your images and verify the integrity and publisher of images you consume.

**Image security scanning** analyses images, detects known vulnerabilities, and provides detailed reports.

**Docker secrets** are a way to securely share sensitive data and are first-class objects in Docker. They’re stored in
the encrypted cluster store, encrypted in-flight when delivered to containers, stored in in-memory filesystems when in
use, and operate a least-privilege model.

Docker works with the major Linux security technologies as well as providing its own extensive and growing set of
security technologies. While the Linux security technologies tend to be complex, the native Docker security technologies
tend to be simple.

A container is a collection of namespaces packaged and ready to use.

Namespaces are about isolation, control groups (_cgroups_) are about setting limits.

You can inspect a node’s client certificate on Linux nodes with the following command:

```shell
sudo openssl x509 \
-in /var/lib/docker/swarm/certificates/swarm-node.crt \
-text
```

The Subject data in the output uses the standard `O`, `OU`, and `CN` fields to specify the Swarm ID, the node’s role,
and the node ID.

- The Organization (`O`) field stores the Swarm ID
- The Organizational Unit (`OU`) field stores the node’s role in the swarm
- The Canonical Name (`CN`) field stores the node’s crypto ID.
