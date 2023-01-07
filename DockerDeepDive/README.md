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
