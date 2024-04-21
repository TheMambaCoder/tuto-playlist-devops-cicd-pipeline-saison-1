#!/bin/bash

# Copy the new jar to the build location
cp -f demo-devops-cicd-project/target/*.jar pipeline-sh-scripts/build/

echo "****************************"
echo "** Building de l'image docker de l'app ***"
echo "****************************"

cd pipeline-sh-scripts/build/ && docker compose -f docker-compose-build-app-image.yml build --no-cache


