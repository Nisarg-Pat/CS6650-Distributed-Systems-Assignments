package server.command;

import server.KeyValueStore;

/**
 * Get Command that returns the value of the key.
 */
public class GetCommand implements Command {

  private final String key;

  public GetCommand(String key) {
    this.key = key;
  }

  @Override
  public Object execute(KeyValueStore db) {
    return db.get(key);
  }
}
