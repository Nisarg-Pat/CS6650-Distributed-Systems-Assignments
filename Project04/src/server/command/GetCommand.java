package server.command;

import server.MyKeyValueDB;
import util.KeyValueDB;

/**
 * Get Command that returns the value of the key.
 */
public class GetCommand implements Command {

  private final String key;

  public GetCommand(String key) {
    this.key = key;
  }

  @Override
  public Object execute(MyKeyValueDB db) {
    return db.get(key);
  }
}
