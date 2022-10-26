# P-2 Multi-threaded Key-Value Store using RPC

## Overview

## List of Features of the Client

## List of Features of the Server


## How to Run
Follow these steps to run the applications using docker:
1) Open the src folder in Terminal.
   > $ cd src
2) Run the deploy.sh bash files to create docker images
   > $ ./deploy.sh
3) Run the TCP/UDP server application using run_server.sh bash file with (tcp/udp) 
   and port number as arguments.
   > $ ./run_server.sh tcp 1111
   > 
   > Or
   > 
   > $ ./run_server.sh udp 5555
4) Run the TCP/UDP client application using run_client.sh bash file with (tcp/udp), server-container-name,
   and port number as arguments.
   > $ ./run_client.sh tcp my-tcp-server 1111
   >
   > Or
   >
   > $ ./run_client.sh udp my-udp-server 5555
5) The server and client are ready to communicate.

Note: If any of the bash files does not execute, try using bash before ./*.sh
> $ bash ./deploy.sh


## How to Use

## Examples and Sample Images


## Limitations

## Citations
