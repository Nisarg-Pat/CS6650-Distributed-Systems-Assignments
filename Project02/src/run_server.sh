#!/bin/bash

PROJECT_NETWORK='project2-network'
SERVER_IMAGE='project2-server-image'
RMI_SERVER_CONTAINER='my-rmi-server'

if [ $# -ne 1 ]
then
  echo "Usage: ./run_server.sh <port-number>"
  exit
fi

winpty docker run -it --rm --name $RMI_SERVER_CONTAINER \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   java server.RMIServerApp "$1"

#winpty docker exec $RMI_SERVER_CONTAINER /bin/bash java server.RMIServerApp "$1"