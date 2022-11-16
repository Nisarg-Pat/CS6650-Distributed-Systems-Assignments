#!/bin/bash

PROJECT_NETWORK='project3-network'
SERVER_IMAGE='project3-server-image'

if [ $# -ne 3 ]
then
  echo "Usage: ./run_server.sh <server-container-name> <port-number> <coordinator-container-name>"
  exit
fi

docker run -it --rm --name "$1" \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   java server.RMIServerApp "$1" "$2" "$3"
