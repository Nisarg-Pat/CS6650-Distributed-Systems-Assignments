package server.command;

import server.MyKeyValueDB;

public class DeleteCommand implements Command {
  private final MyKeyValueDB db;
  private final String key;

  public DeleteCommand(MyKeyValueDB db, String key) {
    this.db = db;
    this.key = key;
  }

  @Override
  public Object execute() {
    return db.delete(key);
  }
}
