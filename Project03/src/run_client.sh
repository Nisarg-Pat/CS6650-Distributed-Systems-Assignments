#!/bin/bash

PROJECT_NETWORK='project3-network'
CLIENT_IMAGE='project3-client-image'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_client.sh <server-container-name> <port-number>"
  exit
fi

docker run -it --rm \
   --network $PROJECT_NETWORK $CLIENT_IMAGE \
   java client.RMIClientApp "$1" "$2"