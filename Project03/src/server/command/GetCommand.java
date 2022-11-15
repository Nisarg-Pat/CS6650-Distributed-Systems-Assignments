package server.command;

import server.MyKeyValueDB;
import util.KeyValueDB;

public class GetCommand implements Command {

  private final MyKeyValueDB db;
  private final String key;

  public GetCommand(MyKeyValueDB db, String key) {
    this.db = db;
    this.key = key;
  }

  @Override
  public Object execute() {
    return db.get(key);
  }
}
