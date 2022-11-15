package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import util.KeyValueDB;

/**
 * A simple implementation of Key-Value Database.
 */
public class MyKeyValueDB {

    private final Map<String, String> map;

    private int wait = 10000;

    //Using a single lock to perform hashmap operations(get/put/delete) to achieve mutual exclusion
    private static final Object LOCK = new Object();

    /**
     * Constructor for the database
     */
    public MyKeyValueDB() {
        super();
        map = new HashMap<>();
    }

    public MyKeyValueDB copy() {

        synchronized (LOCK) {
            MyKeyValueDB copyDB = new MyKeyValueDB();
            for(Map.Entry<String, String> entry: map.entrySet()) {
                copyDB.put(entry.getKey(), entry.getValue());
            }
            return copyDB;
        }
    }

    public String get(String key) {

        synchronized (LOCK) {
            if (!map.containsKey(key)) {
                return "";
            }
            return map.get(key);
        }

    }

    public boolean put(String key, String value) {


        synchronized (LOCK) {
            if (key.isBlank() || value.isBlank()) {
                return false;
            }
            map.put(key, value);
            return true;
        }

    }

    public boolean delete(String key) {

        synchronized (LOCK) {
            if (!map.containsKey(key)) {
                return false;
            }
            map.remove(key);
            return true;
        }

    }

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

    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append("(").append(key).append(", ").append(map.get(key)).append(")\n");
        }
        return sb.toString();
    }
}
