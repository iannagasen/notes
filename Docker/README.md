# Docker

## Glossary
1. **`DOCKERFILE`**
   - Instruction that will use by Docker to build the app
2. **`Dangling Image`**
   - image that is no longer tagged and no repository name
   - occurs when building a new image with a tag that already exists
3. **`Image Digests`**
   - all images get a cryptographic `content hash` aka digest
   - immutable
   - as the digest is the hash of the content of the image
     - it is impossible to change the contents of the image w/o creating a new unique digest.
    - `docker images --digests alpine --format "{{.Digest}}"`
      - sha256:ec050c32e4a6085b423d36ecd025c0d3ff00c38ab93a3d71a460ff1c44fa6d77
4. **`Kernel`**
   - Core of OS,
   - responsible for managing resources, processes and hardware interaction
   - bridge between Hardware and Software
5. **`Docker Volumes`**
   - Exists outside of containers but can be mounted into them
6. **`Restart Policy`**
   - very basic form of self-healing that allows local Docker engine to automatically restart failed containers
   - applied per container
   - imperatively appliend in docker run commands, or declaratively in YAML files
   - restart policies: [always, unless-stopped, on-failure]
## Commands
  - **`docker images`**
    - list all images
    - options:
      - `--filter` / `docker images --filter [ARGS]`
        - args:
          - `dangling` true/false
            - `docker images --filter dangling=true`
            - list all dangling images
          - `before` req image name or ID as argument, 
            - `docker images --filter before=9213`
            - returns all images created before it
          - `after`
            - `docker images --filter since=9213`
            - returns images created after the specified image
          - `label` reqs the label or label value
            - `docker images --filter label=latest`
            - `docker images --filter "is-official=true"`
        - all other filterings may, use `reference`
          - `docker images --filter=reference="*:latest"`
            - list images tag as latest
      - `--format` - using Go template
        - `docker images --format "{{.Size}}"`
        - `docker images --format "{{.Repository}}: {{.Tag}}: {{.Size}}"`
  - **`docker images -q`**
    - list all the id of the images
  - **`docker image prune`**
    - delete all dangling image
  - **`docker image prune -a`**
    - delete all danglingh image + unused(by any containers) images
  - **`docker rmi <ARG>`**
    - ARG could be NAME:TAG, NAME, ID
    - docker rmi
  - **`docker rmi $(docker images -q) -f`**
    - delete all the images
    - same as `docker rmi $(docker images --filter "{{.ID}}") -f`
  - **`docker search <any>`**
    - `docker search nigelpoulton`
    - you can also add `--filter` similar to docker images
      - `docker search alpine --filter "is-official=true"`
  - **`docker pull <IMAGE_REFERENCE>@<DIGEST>`**
    - `docker pull ubuntu@sha256:ec050c32e4a6085b423d36ecd025c0d3ff00c38ab93a3d71a460ff1c44fa6d77`
  - **`docker history <ImageName/ID>`**
    - shows the build history of an image
    - shown from Dockerfile instructions such as "ENV", "EXPOSE", "CMD" ..
  - **`docker info`**
    - check the containerd, runc version, including default registry
  - **`docker manifest`**
    - inspect the manifest list of any image on Docker
    - `docker manifest inspect golang | grep 'architecture\|os'`
  - **`docker ps --filter ancestor=<IMAGE_ID>`**
    - list all containers from a given Image Id
  - **`docker run -it`**
    - run a new container
    - `-it` -> interactive and attach it to current terminal
  - **`docker stop`**
    - stop a container, but the configruation and contents still exists on the Docker host.
      - means that it can be restarted anytime
  - **`docker rm $(docker stop <IMAGENAME/ID>)`**
    - gracefully shutdown a container
    - 1st stop the container, this will return the id and use docker rm
  - **`docker rmi -f $(docker ps -aq)`**
    - Remove all containers, running or stopped
  - **`docker start <id | name>`**
    - start a stopped container
  - **`docker exec -it <id | name> bash`**
    - run bash against the container
  
## FAQs
1. How is Containers smaller, faster and more portable than traditional VMs
   - Shared OS Kernel
     - Containers share the host system Kernel
     - VMs run a complete guest OS on top of a hypervisor, including its own kernel and libraries
   - Resource Efficiency
     - Containers are lightweight because they share the host Kernel and utilize a layered file, 
       - allows multiple containers to share the same underlying resources w/o overhead of running muliple full OS instances
   - Fast Startup and Scaling
     - Containers can start almost instantly because they leverage the existing kernel
     - VMs take longer to start since they require booting up an entire guest OS
2. How to exit to a running container
   - CTRL + PQ or type exit
   - note you will just detach to the current container 

## Running a containerized/dockerized application
1. Pull the dockerized application from github
   - `git clone https://github.com/nigelpoulton/psweb.git`
2. CD to the folder where Dockerfile is located
   - `cd psweb`
3. Inspect the dockerfile, It will have the instruction on how to build the infra
   - `code Dockerfile`
4. Build the Image for the Containerized App with the name:tag `ian_test:latest`
   - `docker build -t ian_test:latest`
   - `-t` tag for naming(name:tag) the image
5. Check if the image is create
   - `docker images`
6. Run the Container from the image
   - `docker run -d --name web1 --publish 8080:8080 ian_test:latest`
   - `-d` -> detached mode
   - `--name` --> name the container
   - `--publish` or `-p` --> expose the port, host:docker
7. Check if the containerized application is running
   - go to localhost:8080/

## Images:
  - dont include kernel.
    - containers share kernel of the host theyre running on
  - Official repositories -> from `https://hub.docker.com/_/`
    - nginx: https://hub.docker.com/_/nginx/
    - redis: https://hub.docker.com/_/redis/
    - https://hub.docker.com/_/mongo/
  - Images and Layers
    - image is a collection of loosely-connected read-only layers
      - each layer comprises one or more files
      - you can see layers by:
        - upon pulling
        - using `docker inspect ubuntu:latest`
    - Multiple images can and do share layers
      - space and performance efficiency
    - Updated versions of files is added as new layers to the image
      - EX:
        - ![](screenshots/2023-08-21-22-34-04.png)
        - here File 7 is an updated version of File 5
  - Image Hashes / Digests
    - Each image is identified by a crypto ID - hash of the manifest file
    - Each layer is identified by a crypto ID - hash of the layer content
    - means that changing the contents of the image or any of its layer
      - associated crypto hashes will change

## Containers

### Container Life Cycle
1. `docker run --name percy -it ubuntu:latest`
   - create a container from ubuntu image and named it percy 
2. `/bin/sh`
   - go to its terminal
3. ```bash
   cd tmp
   ls -l
   echo "Ian Agasen was here" > newfile
   ls -l
   cat newfile
   ```
   - create a new file in tmp directory
4. `CTRL + PQ` 
   - to exit the container
5. `docker stop percy`
   - stop the container
6. `docker ps`
   - check all the running containers, percy should not be listed
7. `docker start percy`
   - start the container
8. `docker exec -it percy bash`  
   - Connect to container using an interactive shell
9. cd tmp
   - check file if it exists.

### Persistent Nature of containers:  
  - The data created in this example is stored on the Docker hosts local filesystem.
    - If the Docker host fails, the data will be lost
  - Containers are designed to be immutable objects and its not a good practice to write data into them
    - For these reasons, Docker provides **volumes**  

### Stopping containers gracefully
1. `docker rm <container> -f`
   - the container is killed immediately without warning
   - it goes straight to SIGKILL
2. `docker stop` + `docker rm`
   - is betterm since it gives the process inside container ~10s to complete any task and gracefully shutdown
   - `docker stop` 
     - sends a `SIGTERM` signal to the main app process inside the container (PID 1)
       - SIGTERM ->  request to terminate 
     - if still running after 10s, it will issue a `SIGKILL`
       - SIGKILL -> terminate with force

### Self-healing containers with restart policy
  - Restart Policy
    - form of self-healing
    - allows local Docker engine to auto restart failed containers
    - applied per-container basis
    - configured imperatively via `docker run` or declaratively via YAML like Docker Compose / K8s
    - Restart Policies
      - `always`
        - always restart a failed container unless its been explicitly stopped
        - `docker run -it --restart always alpine bash`
          - once you exited the container, it will restart it
            - exit command will kill the container
          - feature:
            - when you stop(EXITED) a container, then restart the Docker daemon
              - it will also restart the container
      - `unless-stopped`
        - similar to `--restart always`, but container will not be restarted if was previously stopped and Docker daemon restart
      - `on-failure`
        - will restart a container if it exits with a  non-zero exit code
        - will also restart containers when Docker daemon restarts, including stopped states
    - using YAML:
      - ```yaml
        services:
          myservice:
            <snip>
            restart_policy:
              condition: always | unless-stopped | on-failure
        ```