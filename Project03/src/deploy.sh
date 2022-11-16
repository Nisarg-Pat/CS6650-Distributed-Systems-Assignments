#!/bin/bash

PROJECT_NETWORK='project3-network'
SERVER_IMAGE='project3-server-image'
RMI_SERVER_CONTAINER='my-rmi-server'
COORDINATOR_IMAGE='project3-coordinator-image'
RMI_COORDINATOR_CONTAINER='my-rmi-coordinator'
CLIENT_IMAGE='project3-client-image'
RMI_CLIENT_CONTAINER='my-rmi-client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $RMI_SERVER_CONTAINER 2> /dev/null && docker container rm $RMI_SERVER_CONTAINER 2> /dev/null
docker container stop $RMI_CLIENT_CONTAINER 2> /dev/null && docker container rm $RMI_CLIENT_CONTAINER 2> /dev/null
docker container stop $RMI_COORDINATOR_CONTAINER 2> /dev/null && docker container rm $RMI_COORDINATOR_CONTAINER 2> /dev/null
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