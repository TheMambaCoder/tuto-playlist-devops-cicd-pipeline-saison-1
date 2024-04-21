#!/bin/bash

echo "***************************"
echo "** Build du jar ***********"
echo "***************************"

echo "****** WORKING DIR = $PWD"

docker run --rm  -v  $PWD/demo-devops-cicd-project:/app -v /root/.m2/:/root/.m2/ -w /app maven:3.9.6-eclipse-temurin-17-focal "$@"


