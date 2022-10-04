# P-1 Single Server, Key-Value Store (TCP and UDP)

## Overview

This project implements a basic single threaded server which serves as a key-value store and
responds to the requests made by clients. Two different pairs of applications is implemented.
The TCPClientApp and TCPServerApp communicate using TCP, whereas UDPClientApp and UDPServerAPP 
communicate using UDP. Both servers listen at different sockets.

## List of Features of the Client
These are the features common to the client applications:

* Two instances of Client application are implemented: TCPClientApp and UDPClientApp.
* Clients can accept either of hostname or IP address. This is to be given as a command line 
  argument.
* Clients take the port at which the server is listening. Also given as a command line argument.
* Client is robust to server failure. It displays a timeout if it does not get response 
  within 10 seconds.
* Client is robust to malformed data packets. It displays error message if the response
  is not in proper format.
* Every request from the client and responses given by the server is timestamped to millisecond 
  precision and logged both in Console and in specific log file.

## List of Features of the Server
These are the features common to the server applications:

* Two instances of Server applications are implemented: TCPServerApp and UDPServerApp.
* Server takes the port at which it should listen as a command line argument.
* Server runs forever and can handle single client at a time. Once a client disconnects, 
  new client can connect and interact with the server.
* Every request to the client and its responses given is timestamped to millisecond precision
  and logged both in console and in specific log file.
* Server is robust to malformed data packets. It sends error message as response if the request
  is not in proper format.
* Every requests from the client and responses given by the server is timestamped and logged both
  in Console and in specific log files.
* Around 10 key-value pairs are pre-populated in the server each time a new instance of 
  server application is created.
* Different instances of server have different database. Database is not shared among servers.


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
Once client has started, the list of key-value pairs present in the server is shown in the console.
Client can perform one of the following commands on the terminal:
1) PUT \<key\> \<value\> - Creates a new key-value pair on the server database.
   
   Eg: Command: PUT Hello World
2) GET \<key\> - Returns the value corresponding to the key.
   
   Eg: Command: GET Hello
3) DELETE \<key\> - Deletes the key-value pair from the database.

   Eg: Command: DELETE Hello
4) QUIT - Closes the connection between client and server in TCP. Client Application ends (Both).
5) There is one more special command: ALL - This gives all the key-value pairs present in the 
   database. This is called once everytime a new instance of client application is created.

## Examples and Sample Images

Sample images of client/server interaction along with their corresponding log files 
is present in the res/Screenshots ans res/Log Files folders.

1) TCPServerApp.png and TCPServerLog.txt: Sample files to look how data is shown in TCPServer.
2) TCPClientApp.png and TCPClientLog.txt: Sample files to look how data is entered and 
   response is shown in TCPClient.
3) UDPServerApp.png and UDPServerLog.txt: Sample files to look how data is shown in UDPServer. 
4) UDPClientApp.png and UDPClientLog.txt: Sample files to look how data is entered and
   response is shown in UDPClient.

## Additional Information
1) The server response is of the following format: RESPONSE(\<output\>), where <ouput> is the text 
   response from the server.
2) Maximum of 4 different log files are created: TCPClientLog.txt, TCPServerLog.txt, 
   UDPClientLog.txt, UDPServerLog.txt. Each file contain logs of corresponding applications and are
   similar to the logs shown in the terminal.

## Limitations
1) The TCP Server is single threaded. Thus, it can connect to only one client at a time. 
   If more than one client at a time are connected to the TCPServer, then the client will have 
   to wait till first connection closes. 
   Till then Timeout exception occurs and also response gets out of sync.
2) Currently, the request and response from UDP is not encoded. Methods are present to encode 
   and decode strings but result is improper.

## Citations
1. Chapter 13 - Socket programming
   Object Oriented Programming with Java: Essentials and Applications by Dr. Rajkumar Buyya