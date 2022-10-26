package server;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple Key-Value Database.
 */
public class KeyValueDB {

  private final Map<String, String> map;

  /**
   * Constructor for the database
   */
  public KeyValueDB() {
    map = new HashMap<>();
  }

  /**
   * Gets the value of the given key
   *
   * @param key The key
   * @return The value stored for the key or "" if key is not present
   */
  public String get(String key) {
    if (!map.containsKey(key)) {
      return "";
    }
    return map.get(key);
  }

  /**
   * Puts the (key, value) pair in the database
   *
   * @param key   The key
   * @param value The value
   * @return true if operation is successful else false
   */
  public boolean put(String key, String value) {
    if (key.isBlank() || value.isBlank()) {
      return false;
    }
    map.put(key, value);
    return true;
  }

  /**
   * Deletes the key-value pair in the database
   *
   * @param key The key
   * @return true if operation is successful, else false
   */
  public boolean delete(String key) {
    if (!map.containsKey(key)) {
      return false;
    }
    map.remove(key);
    return true;
  }

  /**
   * Populates the database with some key-value pairs
   */
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

  /**
   * Returns the contents of database
   *
   * @return the contents of database in String format
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String key : map.keySet()) {
      sb.append("(").append(key).append(", ").append(map.get(key)).append(")\n");
    }
    return sb.toString();
  }
}
