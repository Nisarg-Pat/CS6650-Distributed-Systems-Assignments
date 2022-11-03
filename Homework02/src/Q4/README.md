# H-2 Q4 JAVA RMI

## Overview

This project implements a basic JAVA sorting RMI implementation

## How to Run
Follow these steps to run the applications:
1) Open the src folder in Terminal.
   > $ cd src
2) Compile the files:
   > $ javac Q4/*.java
3) Open the rmiregistry on a specific port
   > $ rmiregistry 1254
4) Run the server application
   > $ java Q4/SortingServer 1254

5) Run the TCP client application in another terminal in /src/ folder
   > $ java Q4/SortingClient localhost 1254
6) The server and client are ready to communicate. For multi threaded follow step 4 in another terminal.