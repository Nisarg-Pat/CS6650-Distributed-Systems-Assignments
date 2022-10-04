#!/bin/bash

PROJECT_NETWORK='project1-network'
CLIENT_IMAGE='project1-client-image'
TCP_CLIENT_CONTAINER='my-tcp-client'
UDP_CLIENT_CONTAINER='my-udp-client'

if [ $# -ne 3 ]
then
  echo "Usage: ./run_server.sh <tcp/udp> <server-container-name> <port-number>"
  exit
fi

if [ "$1" == "tcp" ]
then
  # run client docker container with cmd args
  docker run -it --rm --name $TCP_CLIENT_CONTAINER \
   --network $PROJECT_NETWORK $CLIENT_IMAGE \
   java client.TCPClientApp "$2" "$3"
elif [ "$1" == "udp" ]
then
  docker run -it --rm --name $UDP_CLIENT_CONTAINER \
   --network $PROJECT_NETWORK $CLIENT_IMAGE \
   java client.UDPClientApp "$2" "$3"
else
  echo "Usage: ./run_server.sh <tcp/udp> <server-container-name> <port-number>"
  exit
fi