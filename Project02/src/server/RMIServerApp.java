package server;

import util.DataUtils;

import java.rmi.RemoteException;

/**
 * RMI Server Application
 */
public class RMIServerApp {

    /**
     * Main Method to start RMI Server
     *
     * @param args Required args: <port>
     */
    public static void main(String[] args) {
        //Validates the command line arguments
        DataUtils.validateServerArguments(args);

        int port = Integer.parseInt(args[0]);

        //Creating the RMI server and calling start
        try {
            Server server = new RMIServer(port);
            server.start();
        } catch (RemoteException e) {
            System.out.println("Server could not start! Reason: " + e.getMessage());
        }
    }
}
