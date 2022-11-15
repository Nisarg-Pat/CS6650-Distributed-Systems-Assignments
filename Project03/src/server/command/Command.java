package server.command;

import java.io.Serializable;

import server.MyKeyValueDB;

public interface Command extends Serializable {
  Object execute(MyKeyValueDB db);
}
