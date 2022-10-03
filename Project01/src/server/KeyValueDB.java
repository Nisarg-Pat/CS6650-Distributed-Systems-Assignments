package server;

import java.util.HashMap;
import java.util.Map;

public class KeyValueDB {

  Map<String, String> map;

  public KeyValueDB() {
    map = new HashMap<>();
  }

  public String get(String key) {
    if (!map.containsKey(key)) {
      return "";
    }
    return map.get(key);
  }

  public boolean put(String key, String value) {
    if(key.isBlank() || value.isBlank()) {
      return false;
    }
    map.put(key, value);
    return true;
  }

  public boolean delete(String key) {
    if (!map.containsKey(key)) {
      return false;
    }
    map.remove(key);
    return true;
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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(String key: map.keySet()) {
      sb.append("(").append(key).append(", ").append(map.get(key)).append(")\n");
    }
    return sb.toString();
  }
}
