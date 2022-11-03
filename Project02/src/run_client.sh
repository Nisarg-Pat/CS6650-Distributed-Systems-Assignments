#!/bin/bash

PROJECT_NETWORK='project2-network'
CLIENT_IMAGE='project2-client-image'
RMI_CLIENT_CONTAINER='my-rmi-client'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_server.sh <server-container-name> <port-number>"
  exit
fi

docker run -it --rm \
   --network $PROJECT_NETWORK $CLIENT_IMAGE \
   java client.RMIClientApp "$1" "$2"