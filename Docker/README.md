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