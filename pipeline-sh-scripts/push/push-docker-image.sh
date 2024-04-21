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


