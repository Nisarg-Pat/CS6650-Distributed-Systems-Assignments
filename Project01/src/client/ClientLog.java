package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {

  File file;

  public ClientLog() throws IOException {
    file = new File("ClientLog.txt");
  }

  public void log(String s) throws IOException {
    FileWriter writer = new FileWriter(file, true);
    System.out.print(s);
    writer.write(s);
    writer.close();
  }

  public void logln(String s) throws IOException {
    FileWriter writer = new FileWriter(file, true);
    System.out.println(s);
    writer.write(s+"\n");
    writer.close();
  }

  public void logOnly(String s) throws IOException {
    FileWriter writer = new FileWriter(file, true);
    writer.write(s+"\n");
    writer.close();
  }
}
