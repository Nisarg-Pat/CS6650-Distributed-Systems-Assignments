package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {

  File file;

  public Log() throws IOException {
    file = null;
  }

  public void createFile(String s) throws IOException {
    file = new File(s);
    logOnly("\n\n-------------------------------------------------------------------");
  }

  public void log(String s) throws IOException {
    s = DataUtils.getCurrentTime()+": "+s;
    FileWriter writer = new FileWriter(file, true);
    System.out.print(s);
    writer.write(s);
    writer.close();
  }

  public void logln(String s) throws IOException {
    s = DataUtils.getCurrentTime()+": "+s;
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
