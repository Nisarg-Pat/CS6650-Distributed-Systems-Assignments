package util;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of Key-Value Database.
 */
public class MyKeyValueDB extends UnicastRemoteObject implements KeyValueDB {

    private final Map<String, String> map;

    private int wait = 10000;

    /**
     * Constructor for the database
     */
    public MyKeyValueDB() throws RemoteException {
        super();
        map = new HashMap<>();
    }

    @Override
    public synchronized String get(String key) {
//        try {
//            wait = (1-(wait/10000))*10000;
//            Thread.sleep(wait);
//        } catch (InterruptedException e) {
//            System.out.println(e.getMessage());
//        }
        if (!map.containsKey(key)) {
            return "";
        }
        return map.get(key);
    }

    @Override
    public synchronized boolean put(String key, String value) {
//        try {
//            wait = (1-(wait/10000))*10000;
//            Thread.sleep(wait);
//        } catch (InterruptedException e) {
//            System.out.println(e.getMessage());
//        }
        if (key.isBlank() || value.isBlank()) {
            return false;
        }
        map.put(key, value);
        return true;
    }

    @Override
    public synchronized boolean delete(String key) {
//        try {
//            wait = (1-(wait/10000))*10000;
//            Thread.sleep(wait);
//        } catch (InterruptedException e) {
//            System.out.println(e.getMessage());
//        }
        if (!map.containsKey(key)) {
            return false;
        }
        map.remove(key);
        return true;
    }

    @Override
    public void populate() {
        map.put("Hello", "World");
        map.put("Accept", "Refuse");
        map.put("Answer", "Question");
        map.put("Abundant", "Scarce");
        map.put("Calm", "Furious");
        map.put("Expensive", "Cheap");
        map.put("Friend", "Enemy");
        map.put("Happy", "Sad");
        map.put("Inferior", "Superior");
        map.put("Possible", "Impossible");
        map.put("Serious", "Trivial");
        map.put("Strong", "Weak");
    }

    @Override
    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append("(").append(key).append(", ").append(map.get(key)).append(")\n");
        }
        return sb.toString();
    }
}
