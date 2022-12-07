#!/bin/bash

PROJECT_NETWORK='project3-network'
COORDINATOR_IMAGE='project3-server-image'
RMI_COORDINATOR_CONTAINER='my-rmi-coordinator'

if [ $# -ne 0 ]
then
  echo "Usage: ./run_coordinator.sh"
  exit
fi

docker run -it --rm --name $RMI_COORDINATOR_CONTAINER \
   --network $PROJECT_NETWORK $COORDINATOR_IMAGE \
   java server.ServerApp "$RMI_COORDINATOR_CONTAINER"