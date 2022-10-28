#!/bin/bash

PROJECT_NETWORK='project1-network'
CLIENT_IMAGE='project1-client-image'
TCP_CLIENT_CONTAINER='my-tcp-client'
UDP_CLIENT_CONTAINER='my-udp-client'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_server.sh <server-container-name> <port-number>"
  exit
fi

winpty docker run -it --rm --name $TCP_CLIENT_CONTAINER \
   --network $PROJECT_NETWORK $CLIENT_IMAGE \
   java client.RMIClientApp "$1" "$2"