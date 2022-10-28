#!/bin/bash

PROJECT_NETWORK='project2-network'
SERVER_IMAGE='project2-server-image'
REGISTRY_CONTAINER='my-registry-container'

if [ $# -ne 1 ]
then
  echo "Usage: ./run_rmiregistry.sh <port-number>"
  exit
fi

docker run -it --rm --name $REGISTRY_CONTAINER \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   rmiregistry "$1"