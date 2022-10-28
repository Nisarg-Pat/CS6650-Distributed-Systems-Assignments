package client;

import util.DataUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;

/**
 * RMI Client Application
 */
public class RMIClientApp {
    /**
     * Main Method to start RMI Client
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
            Client client = new RMIClient(host, port);
            client.execute();
        } catch (IOException | NotBoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
