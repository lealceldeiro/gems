# Docker Deep Dive

Main notes taken from the book [Docker Deep Dive](https://www.amazon.com/Docker-Deep-Dive-Nigel-Poulton/dp/1521822808)

## Part 1

### Chapter 4

When running a container in iterative mode (`-it`) the shell prompt is switched into the terminal of the container. To exit the container without terminating it, press **Ctrl**-**PQ**.

You can attach your shell to the terminal of a running container with the `docker container exec` command.

## Part 2

### Chapter 5: The Docker Engine

The Docker engine is modular in design and based heavily on open-standards from the OCI.

The Docker *daemon* implements the Docker API which is currently a rich, versioned, HTTP API that has developed alongside the rest of the Docker project.

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

## Commands to work with images

- `docker image pull`: download images
- `docker image ls`: list all of the images stored in Docker host’s local image cache. The `--digests` flag displays 
the SHA256 digests.
- `docker image inspect`: show the details of an image -- layer data and metadata
- `docker manifest inspect`: show the manifest list of any image stored on Docker Hub
- `docker buildx` is a Docker CLI plugin that extends the Docker CLI to support multi-arch builds
- `docker image rm`: delete images