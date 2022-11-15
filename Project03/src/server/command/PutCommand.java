package server.command;

import server.MyKeyValueDB;

public class PutCommand implements Command{
  private final String key;
  private final String value;

  public PutCommand(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public Object execute(MyKeyValueDB db) {
    return db.put(key, value);
  }
}
