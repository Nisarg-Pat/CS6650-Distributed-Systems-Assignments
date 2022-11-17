# P-3 Distributed Key-Value Store

## Overview

This project implements a basic multi-threaded server which serves as a key-value store and
responds to the requests made by clients using Remote Method Invocation in Java. The remote object is binded to 
rmiregistry created before starting the server.
Multiple servers can be replicated and client can acces any of them to perform consistent operations on the system.

## List of Features of the Client
These are the features of the client applications:

* Clients accept the host name and the port number of the rmiregistry.
* Clients are robust to server failure. It displays a timeout if it does not get response
  within 20 seconds.
* Clients are robust to malformed data packets. It displays error message if the response
  is not in proper format.
* Every request from the client and responses given by the server is timestamped to millisecond
  precision and logged in client console.
* Client can change the server through which it communicates in between the run.

## List of Features of the Server
These are the features of server application:

* Multiple replicas of server can be available at any time.
* Data is consistent among all the servers with the help of distributed transactions (two-phase commit protocol).
* Server can close(crash) or restart with the data remaining consistent with the running servers.
* A Coordinator server is present that maintains a list of all the running servers.
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
3) Run the Coordinator server application using run_coordinator.sh bash file. NOTE: The coordinator has to run before starting any other server.
   > $ ./run_coordinator.sh
4) Run the RMI server application using run_server.sh bash file by providing the server-name and port. Start multiple such servers on different terminals. 5 such servers can be
   > $ ./run_server.sh server1 1231

   > $ ./run_server.sh server2 1232

   > $ ./run_server.sh server3 1233

   > $ ./run_server.sh server4 1234

   > $ ./run_server.sh server5 1235
5) Run the RMI client application using run_client.sh bash file with any running server-name,
   and port number as arguments in a different terminal.
   > $ ./run_client.sh server1 1231
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
4) CONNECT - Connects the client to a different server given the server-name and the port number.

   Eg: Command: CONNECT server5 1235
5) QUIT - Closes the connection between client and server in TCP. Client Application ends (Both).
6) There is one more special command: ALL - This gives all the key-value pairs present in the
   database. This is called once everytime a new instance of client application is created.

## Examples and Sample Images

Sample images of client/server interaction is present in the res/Screenshots folder.
1) Coordinator.png: How to run the Coordinator
2) RMIServer.png: How to run the RMIServerApp
3) RMIClient.png: Sample images showing to run RMIClient and how data is shown in RMIClientApp.


## Limitations
1) The operations on different servers for the two phase commit protocol is linear and on single thread rather than multi thread which decreases performance.

## Citations
1. Two Phase commit protocol: Distributed Systems: Concepts and Design, 5th edition
2. Java RMI: https://www.cs.uic.edu/~troy/fall04/cs441/rmi/calc/index.html
3. How to synchronize: https://www.baeldung.com/java-synchronized