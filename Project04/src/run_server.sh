#!/bin/bash

PROJECT_NETWORK='project4-network'
SERVER_IMAGE='project4-server-image'
COORDINATOR_NAME='my-paxos-coordinator'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_server.sh <server-container-name> <port-number>"
  exit
fi

docker run -it --rm --name "$1" \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   java server.ServerApp "$1" "$2" $COORDINATOR_NAME
