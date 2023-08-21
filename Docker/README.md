# Docker

## Glossary
1. **`DOCKERFILE`**
   - Instruction that will use by Docker to build the app

## Commands

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
