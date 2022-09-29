package server;

import java.util.HashMap;
import java.util.Map;

public class KeyValueDB {

  Map<String, String> map;

  public KeyValueDB() {
    map = new HashMap<>();
  }

  public String get(String key) {
    if(!map.containsKey(key)) {
      return "";
    }
    return map.get(key);
  }

  public boolean put(String key, String value) {
    map.put(key, value);
    return true;
  }

  public boolean delete(String key) {
    map.remove(key);
    return true;
  }
}
