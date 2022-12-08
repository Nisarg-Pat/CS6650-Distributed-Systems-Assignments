package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import util.DataUtils;

/**
 * PAXOS Client Application
 */
public class PaxosClientApp {
    /**
     * Main Method to start PAXOS Client
     *
     * @param args Required args: <hostname> <port>
     */
    public static void main(String[] args) {

        //Validates the command line arguments
        DataUtils.validateClientArguments(args);

        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            //Creating a TCP Client and calling execute
            Client client = new PaxosClient(host, port);
            client.execute();
        } catch (NotBoundException | RemoteException e) {
            System.out.println("Error starting client: "+e.getMessage());
        }
    }
}
