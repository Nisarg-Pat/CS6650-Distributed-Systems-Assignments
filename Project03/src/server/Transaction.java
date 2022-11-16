package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import server.command.Command;

class Transaction implements Serializable {
  private final ServerHeader callerHeader;
  private final String id;
  private final List<Command> commandList;
  private final List<Object> result;

  Transaction(String id, ServerHeader callerHeader) {
    this.id = id;
    this.callerHeader = callerHeader;
    commandList = new ArrayList<>();
    result = new ArrayList<>();
  }

  public void addCommand(Command command) {
    commandList.add(command);
  }

  public String getId() {
    return id;
  }

  public List<Command> getCommandList() {
    return commandList;
  }

  public List<Object> getResult() {
    return result;
  }

  public List<Object> execute(MyKeyValueDB db) {
    for(Command command: commandList) {
      result.add(command.execute(db));
    }
    return result;
  }

  public ServerHeader getCallerHeader() {
    return callerHeader;
  }
}
