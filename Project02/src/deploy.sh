#!/bin/bash

PROJECT_NETWORK='project1-network'
SERVER_IMAGE='project1-server-image'
TCP_SERVER_CONTAINER='my-tcp-server'
UDP_SERVER_CONTAINER='my-udp-server'
CLIENT_IMAGE='project1-client-image'
TCP_CLIENT_CONTAINER='my-tcp-client'
UDP_CLIENT_CONTAINER='my-udp-client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $TCP_SERVER_CONTAINER 2> /dev/null && docker container rm $TCP_SERVER_CONTAINER 2> /dev/null
docker container stop $UDP_SERVER_CONTAINER 2> /dev/null && docker container rm $UDP_SERVER_CONTAINER 2> /dev/null
docker container stop $TCP_CLIENT_CONTAINER 2> /dev/null && docker container rm $TCP_CLIENT_CONTAINER 2> /dev/null
docker container stop $UDP_CLIENT_CONTAINER 2> /dev/null && docker container rm $UDP_CLIENT_CONTAINER 2> /dev/null
docker network rm $PROJECT_NETWORK 2> /dev/null

# only cleanup
if [ "$1" == "cleanup-only" ]
then
  exit
fi

# create a custom virtual network
echo "----------creating a virtual network----------"
docker network create $PROJECT_NETWORK

# build the images from Dockerfile
echo "----------Building images----------"
docker build -t $CLIENT_IMAGE --target client-build .
docker build -t $SERVER_IMAGE --target server-build .