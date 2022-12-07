package server.command;

import server.KeyValueStore;

/**
 * Delete command that removes the key.
 */
public class DeleteCommand implements Command {
  private final String key;

  public DeleteCommand(String key) {
    this.key = key;
  }

  @Override
  public Object execute(KeyValueStore db) {
    return db.delete(key);
  }
}
