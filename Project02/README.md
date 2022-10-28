# P-2 Multi-threaded Key-Value Store using RPC

## Overview

This project implements a basic multi-threaded server which serves as a key-value store and
responds to the requests made by clients using Remote Method Invocation in Java. The remote object is binded to 
rmiregistry created before starting the server.

## List of Features of the Client
These are the features of the client applications:

* Clients accept the host name and the port number of the rmiregistry.
* Clients are robust to server failure. It displays a timeout if it does not get response
  within 10 seconds.
* Clients are robust to malformed data packets. It displays error message if the response
  is not in proper format.
* Every request from the client and responses given by the server is timestamped to millisecond
  precision and logged in client console.

## List of Features of the Server
These are the features of server application:

* Server takes the port number of the rmiregistry on which it has to bind the service
  as a command line argument. Rmiregistry and server run on the same machine.
* Server binds the KeyStoreDB object to the rmiregistry.
* The KeyStoreDB can perform 3 actions: Get/Put/Delete.
* KeyStoreDB can handle multiple clients at the same time, with mutual exclusion 
  among the actions making them thread safe.
* KeyStoreDB is initialized with 10 key-value pairs and the initial contents of the DB are displayed 
  to the clients who just logged in.

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

Sample images of client/server interaction is present in the res/Screenshots folder.

1) RMIClientApp.png: Sample files to look how data is shown in RMIServerApp.

## Citations
1. Java RMI: https://www.cs.uic.edu/~troy/fall04/cs441/rmi/calc/index.html
2. How to synchronize: https://www.baeldung.com/java-synchronized