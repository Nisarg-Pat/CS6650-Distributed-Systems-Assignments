package server;

import java.rmi.RemoteException;

import util.DataUtils;

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

        //Creating the RMI server/coordinator and calling start
        try {
            if(args.length == 1) {
                String host = args[0];
                new MyCoordinatorServer(host).start();
            } else if(args.length == 3){
                DataUtils.validateServerArguments(args);
                String host = args[0];
                int port = Integer.parseInt(args[1]);
                String coordinatorHost = args[2];
                new RMIServer(host, port, coordinatorHost).start();
            } else {
                System.out.println("Improper number of arguments");
            }
        } catch (RemoteException e) {
            System.out.println("Server could not start! Reason: " + e.getMessage());
        }
    }
}
