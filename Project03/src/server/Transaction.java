package server;

import java.util.ArrayList;
import java.util.List;

import server.command.Command;

class Transaction {
  private final int id;
  private final List<Command> commandList;

  Transaction(int id) {
    this.id = id;
    commandList = new ArrayList<>();
  }

  public void addCommand(Command command) {
    commandList.add(command);
  }

  public int getId() {
    return id;
  }

  public List<Command> getCommandList() {
    return commandList;
  }
}
