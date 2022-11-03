# H-2 Q1 Single and Multi Threaded Server

## Overview

This project implements a basic single and multi-threaded server which interacts with a single threaded client using TCP protocol.

## How to Run
Follow these steps to run the applications:
1) Open the src folder in Terminal.
   > $ cd src
2) Compile the files:
   > $ javac Q1/client/*.java Q1/server/*.java Q1/util/*.java
3) Run the TCP server application either in single threaded or multi-threaded mode
   and port number as arguments.
   > $ java Q1/server/TCPServerApp single 1234
   >
   > Or
   >
   > $ java Q1/server/TCPServerApp multi 1234
4) Run the TCP client application in another terminal in /src/ folder
   > $ java Q1/client/TCPClientApp localhost 1234
5) The server and client are ready to communicate. For multi threaded follow step 4 in another terminal.


## How to Use
Type any String, and server will respond with reversed and case changed String.

Eg:

Command: Hello

Response: OLLEh

Type QUIT to exit the client.

For multi-threaded, 


## Citations
1. Chapter 13 and 14
   Object Oriented Programming with Java: Essentials and Applications by Dr. Rajkumar Buyya