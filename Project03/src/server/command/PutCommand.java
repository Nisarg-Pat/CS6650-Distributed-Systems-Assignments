package server.command;

import server.MyKeyValueDB;

public class PutCommand implements Command{
  private final MyKeyValueDB db;
  private final String key;
  private final String value;

  public PutCommand(MyKeyValueDB db, String key, String value) {
    this.db = db;
    this.key = key;
    this.value = value;
  }

  @Override
  public Object execute() {
    return db.put(key, value);
  }
}
