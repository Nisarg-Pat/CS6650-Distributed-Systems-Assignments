package client;

import util.KeyValueDB;
import util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * RMI implementation of Client.
 */
public class RMIClient implements Client {

    protected final Scanner scanner;

    protected static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

    protected final Log clientLog;

    private final String host;
    private final int port;

    private static boolean connected;

    private static KeyValueDB db;

    /**
     * Constructor for RMIClient
     *
     * @param host hostname of the rmiregistry
     * @param port port of the rmiregistry
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RMIClient(String host, int port) throws RemoteException, NotBoundException {
        this.host = host;
        this.port = port;

        scanner = new Scanner(System.in);
        clientLog = new Log();

        Registry registry = LocateRegistry.getRegistry(this.host, this.port);
        db = (KeyValueDB) registry.lookup("KeyValueDBService");
        this.connected = true;

        //Creating a log file
        clientLog.createFile("TCPClientLog.txt");
//        clientLog.logln(String.format("Connected to server at %s:%s", host, port));
    }

    @Override
    public void execute() throws RemoteException {
        try {
            clientLog.logln("Database Content(Key, Value):\n" + db.getString());
        } catch (RemoteException e) {
            clientLog.logln(e.getMessage());
        }

        clientLog.logln("Possible commands: PUT/GET/DELETE/QUIT\n");
        String input = "";
        try {
            while (connected) {
                //Taking client request
                clientLog.log("Command: ");
                input = scanner.nextLine();
                clientLog.logOnly(input);
                if (input.equals("QUIT")) {
                    break;
                }
                //Requesting to the server
                String output = processRequest(input);
                clientLog.logln("Response: " + output);
            }
        } catch (Exception e) {
            //Empty catch for cntl+C
        }
        clientLog.logln("Connection to server closed!");
    }

    private String processRequest(String input) {
        if (input.equals("ALL")) {
            //Gives list of all key-value pair in the database
            //Automatically called initially by the client
            return "Database Content(Key, Value):\n" + db.toString();
        }
        String[] data = input.split(" ");
        String command = data[0];
        switch (command) {
            case "PUT":
                if (checkPut(data)) {
                    String key = data[1];
                    String value = data[2];
                    return getOutput(new PutRunnable(key, value));
                } else {
                    //Improper arguments after PUT
                    return "Invalid format for PUT. Expected: PUT <key> <value>";
                }
            case "GET":
                if (checkGet(data)) {
                    String key = data[1];
                    return getOutput(new GetRunnable(key));
                } else {
                    //Improper arguments after GET
                    return "Invalid format for GET. Expected: GET <key>";
                }
            case "DELETE":
                if (checkDelete(data)) {
                    String key = data[1];
                    return getOutput(new DeleteRunnable(key));
                } else {
                    //Improper arguments after DELETE
                    return "Invalid format for DELETE. Expected: DELETE <key>";
                }
            default:
                //Improper request from client or command not recognized
                return "Received malformed request from client!!";
        }
    }

    private String getOutput(DBRunnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        long timeoutTime = System.currentTimeMillis() + TIMEOUT;
        while (thread.isAlive()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= timeoutTime) {
                return "Server Timeout! Please try requesting again\n";
            }
        }
        return runnable.returnValue;
    }

    /**
     * Checks the validity of DELETE command. Required: DELETE <key>
     *
     * @param data DELETE command
     * @return true if DELETE command is of valid format
     */
    private boolean checkDelete(String[] data) {
        return data.length == 2;
    }

    /**
     * Checks the validity of GET command. Required: GET <key>
     *
     * @param data GET command
     * @return true if GET command is of valid format
     */
    private boolean checkGet(String[] data) {
        return data.length == 2;
    }

    /**
     * Checks the validity of PUT command. Required: PUT <key> <value>
     *
     * @param data PUT command
     * @return true if PUT command is of valid format
     */
    private boolean checkPut(String[] data) {
        return data.length == 3;
    }

    abstract static class DBRunnable implements Runnable {
        String returnValue;

        DBRunnable() {
            returnValue = "";
        }
    }

    static class PutRunnable extends DBRunnable {

        String key;
        String value;

        PutRunnable(String key, String value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                boolean res = db.put(key, value);
                if (res) {
                    //PUT successful
                    returnValue = String.format("(%s, %s) added successfully", key, value);
                } else {
                    //PUT failed. It can be because of blank or improper key or value.
                    returnValue = String.format("Cannot put (%s, %s) in database. Please check the key and value.", key, value);
                }
            } catch (RemoteException e) {
                returnValue = "Host Disconnected! Try Again!!";
                connected = false;
            }
        }
    }

    static class GetRunnable extends DBRunnable {

        String key;

        GetRunnable(String key) {
            super();
            this.key = key;
        }

        @Override
        public void run() {
            try {
                String res = db.get(key);
                if (res.equals("")) {
                    //GET failed. It can occur if the key is not present in the database.
                    returnValue = key + " is not present in database.";
                } else {
                    //GET successful
                    returnValue = res;
                }
            } catch (RemoteException e) {
                returnValue = "Host Disconnected! Try Again!!";
                connected = false;
            }
        }
    }

    static class DeleteRunnable extends DBRunnable {

        String key;

        DeleteRunnable(String key) {
            super();
            this.key = key;
        }

        @Override
        public void run() {
            try {
                boolean res = db.delete(key);
                if (res) {
                    //DELETE successful
                    returnValue = "Successfully deleted " + key;
                } else {
                    //DELETE failed. It can occur if the key is not present in the database.
                    returnValue = key + " is not present in database";
                }
            } catch (RemoteException e) {
                returnValue = "Host Disconnected! Try Again!!";
                connected = false;
            }
        }
    }
}
