#!/bin/bash

PROJECT_NETWORK='project4-network'
CLIENT_IMAGE='project4-client-image'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_client.sh <server-container-name> <port-number>"
  exit
fi

docker run -it --rm \
   --network $PROJECT_NETWORK $CLIENT_IMAGE \
   java client.PaxosClientApp "$1" "$2"