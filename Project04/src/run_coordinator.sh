#!/bin/bash

PROJECT_NETWORK='project4-network'
COORDINATOR_IMAGE='project4-server-image'
PAXOS_COORDINATOR_CONTAINER='my-paxos-coordinator'

if [ $# -ne 0 ]
then
  echo "Usage: ./run_coordinator.sh"
  exit
fi

docker run -it --rm --name $PAXOS_COORDINATOR_CONTAINER \
   --network $PROJECT_NETWORK $COORDINATOR_IMAGE \
   java server.ServerApp "$PAXOS_COORDINATOR_CONTAINER"