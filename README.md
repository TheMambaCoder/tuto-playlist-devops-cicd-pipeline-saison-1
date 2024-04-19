# Quelques commandes utilié dans la playliste

## Installation de Java et Jenkins 

```
sudo yum install java-11-openjdk -y && \
sudo wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat-stable/jenkins.repo && \ 
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key && \
sudo yum -y install jenkins && \
sudo systemctl start jenkins && \
sudo systemctl enable jenkins && \
sudo firewall-cmd --permanent --zone=public --add-port=8080/tcp && \
sudo firewall-cmd --reload
```

## Installation de docker 

```
sudo yum install -y yum-utils && \
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo && \
sudo yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin && \
sudo systemctl start docker && \
sudo usermod -aG docker vagrant
```

## Fichier docker-compose pour pour la création d'une instance Gitlab

```
version: '3.6'
services:
  gitlab:
    image: zengxs/gitlab:latest
    container_name: gitlab
    restart: always
    hostname: 'gitlab.example.com'
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        # Add any other gitlab.rb configuration here, each on its own line
        external_url 'http://gitlab.example.com'
    ports:
      - '80:80'
      - '443:443'
    volumes:
      - '$GITLAB_HOME/config:/etc/gitlab'
      - '$GITLAB_HOME/logs:/var/log/gitlab'
      - '$GITLAB_HOME/data:/var/opt/gitlab'
    shm_size: '256m'
```

## Changer le password de l'user root de Gitlab
`docker exec -it gitlab gitlab-rake "gitlab:password:reset[root]"`  

## Script de build/test du l'appli Java 17 / Springboot 3.2

```
#!/bin/bash

echo "***************************"
echo "** Build du jar ***********"
echo "***************************"

echo "****** WORKING DIR = $PWD"

docker run --rm  -v  $PWD/demo-devops-cicd-project:/app -v /root/.m2/:/root/.m2/ -w /app maven:3.9.6-eclipse-temurin-17-focal "$@"
```

## Exemple d'appel 

```
./pipeline-sh-scripts/build/mvn-cmd.sh mvn -B -DskipTests clean package
```

## Dockerfile de l'app

```
FROM openjdk:18-ea-17-slim-buster

RUN mkdir /app

COPY *.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

```

## Script build-docker-image.sh 

```
#!/bin/bash

# Copy the new jar to the build location
cp -f demo-devops-cicd-project/target/*.jar pipeline-sh-scripts/build/

echo "****************************"
echo "** Building de l'image docker de l'app ***"
echo "****************************"

cd pipeline-sh-scripts/build/ && docker compose -f docker-compose-build-app-image.yml build --no-cache
```

## Script docker-compose-build-app-image.yml

```
version: '3'
services:
  app:
    image: "devops-cicd-project:$BUILD_TAG"
    build:
      context: .
      dockerfile: Dockerfile-java-app
```

## Script de push de l'image docker 

```
#!/bin/bash

echo "*************************************"
echo "** Pushing the docker image ***"
echo "**************************************"

IMAGE="devops-cicd-project"

echo "** Se connecter sur le registry docker ***"
docker login -u momsboy -p $PASS
echo "*** Tagguer l image docker ***"
docker tag $IMAGE:$BUILD_TAG momsboy/$IMAGE:$BUILD_TAG
echo "*** Pushing the image to the registry ***"

docker push momsboy/$IMAGE:$BUILD_TAG
```

## Exemple utilisation du pluggin Ansible-playbook

```
ansiblePlaybook (
              colorized: true,
              playbook: "/var/lib/jenkins/workspace/my_pipeline/ansible-deploy-app/install_app.yml",
              hostKeyChecking: false,
              inventory: "/var/lib/jenkins/workspace/my_pipeline/ansible-deploy-app/inventory",
              extras: "-e 'docker_image_tag=$BUILD_TAG'"
              )
```

## Inventory dev serveur

```
all:
  hosts:
    dev:
     ansible_host: 192.168.41.163
     ansible_user: vagrant
     ansible_private_key_file: /var/lib/jenkins/workspace/id_rsa_jenkins
     ansible_ssh_common_args: "-o StrictHostKeyChecking=no"
```

## Exemple de playbook 

```
---
- name: Install The app docker image in the dev environment
  hosts: dev
  vars:
    docker_image_prefix: momsboy/devops-cicd-project

  tasks:
  - name: "suppression de l'image de l'app"
    shell: "docker rm -f monApp"

  - name: install de l'image docker de l'app
    shell: "docker run -d --name monApp -p 8080:8080 {{ docker_image_prefix }}:{{ docker_image_tag }}"

  - name: attente de la disponibilite de l'app
    uri:
     url: "http://127.0.0.1:8080/test"
     status_code: 200
    register: result
    until: result.status == 200
    retries: 100
    delay: 1

```

## 
