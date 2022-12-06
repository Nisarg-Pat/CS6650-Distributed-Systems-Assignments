#!/bin/bash

PROJECT_NETWORK='project3-network'
SERVER_IMAGE='project3-server-image'
COORDINATOR_NAME='my-rmi-coordinator'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_server.sh <server-container-name> <port-number>"
  exit
fi

docker run -it --rm --name "$1" \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   java server.RMIServerApp "$1" "$2" $COORDINATOR_NAME
