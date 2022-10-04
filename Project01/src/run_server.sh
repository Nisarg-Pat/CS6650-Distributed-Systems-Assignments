#!/bin/bash

PROJECT_NETWORK='project1-network'
SERVER_IMAGE='project1-server-image'
TCP_SERVER_CONTAINER='my-tcp-server'
UDP_SERVER_CONTAINER='my-udp-server'

if [ $# -ne 2 ]
then
  echo "Usage: ./run_server.sh <tcp/udp> <port-number>"
  exit
fi

if [ "$1" == "tcp" ]
then
  # run client docker container with cmd args
  docker run -it --rm --name $TCP_SERVER_CONTAINER \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   java server.TCPServerApp "$2"
elif [ "$1" == "udp" ]
then
  docker run -it --rm --name $UDP_SERVER_CONTAINER \
   --network $PROJECT_NETWORK $SERVER_IMAGE \
   java server.UDPServerApp "$2"
else
  echo "Usage: ./run_server.sh <tcp/udp> <port-number>"
  exit
fi