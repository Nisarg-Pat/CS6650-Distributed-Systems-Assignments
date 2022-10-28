#!/bin/bash

PROJECT_NETWORK='project2-network'
SERVER_IMAGE='project2-server-image'
RMI_SERVER_CONTAINER='my-rmi-server'

if [ $# -ne 0 ]
then
  echo "Usage: ./run_server.sh"
  exit
fi

docker run -it --rm --name $RMI_SERVER_CONTAINER \
   --network $PROJECT_NETWORK $SERVER_IMAGE