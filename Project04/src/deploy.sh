#!/bin/bash

PROJECT_NETWORK='project4-network'
SERVER_IMAGE='project4-server-image'
PAXOS_SERVER_CONTAINER='my-paxos-server'
COORDINATOR_IMAGE='project4-coordinator-image'
PAXOS_COORDINATOR_CONTAINER='my-paxos-coordinator'
CLIENT_IMAGE='project4-client-image'
PAXOS_CLIENT_CONTAINER='my-paxos-client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $PAXOS_SERVER_CONTAINER 2> /dev/null && docker container rm $PAXOS_SERVER_CONTAINER 2> /dev/null
docker container stop $PAXOS_CLIENT_CONTAINER 2> /dev/null && docker container rm $PAXOS_CLIENT_CONTAINER 2> /dev/null
docker container stop $PAXOS_COORDINATOR_CONTAINER 2> /dev/null && docker container rm $PAXOS_COORDINATOR_CONTAINER 2> /dev/null
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