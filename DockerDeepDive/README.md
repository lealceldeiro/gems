# Docker Deep Dive

Main notes taken from the book [Docker Deep Dive](https://www.amazon.com/Docker-Deep-Dive-Nigel-Poulton/dp/1521822808)

## Part 1

### Chapter 4

When running a container in iterative mode (`-it`) the shell prompt is switched into the terminal of the container. To exit the container without terminating it, press **Ctrl**-**PQ**.

You can attach your shell to the terminal of a running container with the `docker container exec` command.

## Part 2

### Chapter 5

The Docker engine is modular in design and based heavily on open-standards from the OCI.

The Docker *daemon* implements the Docker API which is currently a rich, versioned, HTTP API that has developed alongside the rest of the Docker project.

Container execution is handled by *containerd*.

By default, Docker uses `runc` as its default container runtime.
