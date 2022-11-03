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
3) Run the RMI server application using run_server.sh bash file
   > $ ./run_server.sh
4) The RMIRegistry runs on my-rmi-server container and at port 1234. 
   Run the RMI client application using run_client.sh bash file with server-container-name,
   and port number as arguments in a different terminal.
   > $ ./run_client.sh my-rmi-server 1234
5) The server and client are ready to communicate. You can create multiple clients and interact with server.

Note: If any of the bash files does not execute, try using bash before ./*.sh
> $ bash ./deploy.sh

Note: I have changed the permissions on .sh files to make it executable, and I am using Windows.
but since other operating system can be used, if permission denied error occurs, please change the permission by doing:
> $ chmod 755 (filename).sh

Note: If any such error occurs: "./run_server.sh: line 2: $'\r': command not found", 
please change the Line seperator from CRLF to LF of all the ./sh files.

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
1) RMIServer.png: How to run the RMIServerApp
2) RMIClient1.png and RMIClient2.png: Sample images showing to run RMIClient and how data is shown in RMIClientApp.


## Limitations
1) The docker image for running server has port predefined as 1234. I was not able to make it dynamic. 
   So run_server currently does not take any arguments but runs the rmiregistry on fixed port 1234.

## Citations
1. Java RMI: https://www.cs.uic.edu/~troy/fall04/cs441/rmi/calc/index.html
2. How to synchronize: https://www.baeldung.com/java-synchronized