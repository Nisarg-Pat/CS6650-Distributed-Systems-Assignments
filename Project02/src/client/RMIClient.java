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

public class RMIClient implements Client {

    protected final Scanner scanner;

    protected static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

    protected final Log clientLog;

    private final String host;
    private final int port;

    public static KeyValueDB db;

    public RMIClient(String host, int port) throws RemoteException, NotBoundException {
        this.host = host;
        this.port = port;

        scanner = new Scanner(System.in);
        clientLog = new Log();

        Registry registry = LocateRegistry.getRegistry(this.host, this.port);
        db = (KeyValueDB) registry.lookup("KeyValueDBService");

        //Creating a log file
        clientLog.createFile("TCPClientLog.txt");
        clientLog.logln(String.format("Connected to server at %s:%s", host, port));
    }

    @Override
    public void execute() throws IOException {
        clientLog.logln("Database Content(Key, Value):\n" + db.getString());
        clientLog.logln("Possible commands: PUT/GET/DELETE/QUIT\n");
        String input = "";
        while (true) {
            //Taking client request
            clientLog.log("Command: ");
            input = scanner.nextLine();
            clientLog.logOnly(input);
            if (input.equals("QUIT")) {
                break;
            }
            //Requesting to the server
            String output = processRequest(input);
            clientLog.logln(output);
        }
    }

    public String processRequest(String input) throws RemoteException {
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
                    boolean res = db.put(key, value);
                    if (res) {
                        //PUT successful
                        return String.format("(%s, %s) added successfully", key, value);
                    } else {
                        //PUT failed. It can be because of blank or improper key or value.
                        return String.format("Cannot put (%s, %s) in database. Please check the key and value.", key, value);
                    }
                } else {
                    //Improper arguments after PUT
                    return "Invalid format for PUT. Expected: PUT <key> <value>";
                }
            case "GET":
                if (checkGet(data)) {
                    String key = data[1];
                    String res = db.get(key);
                    if (res.equals("")) {
                        //GET failed. It can occur if the key is not present in the database.
                        return key + " is not present in database.";
                    } else {
                        //GET successful
                        return res;
                    }
                } else {
                    //Improper arguments after GET
                    return "Invalid format for GET. Expected: GET <key>";
                }
            case "DELETE":
                if (checkDelete(data)) {
                    String key = data[1];
                    boolean res = db.delete(key);
                    if (res) {
                        //DELETE successful
                        return "Successfully deleted " + key;
                    } else {
                        //DELETE failed. It can occur if the key is not present in the database.
                        return key + " is not present in database";
                    }
                } else {
                    //Improper arguments after DELETE
                    return "Invalid format for DELETE. Expected: DELETE <key>";
                }
            default:
                //Improper request from client or command not recognized
                return "Received malformed request from client!!";
        }
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
}
