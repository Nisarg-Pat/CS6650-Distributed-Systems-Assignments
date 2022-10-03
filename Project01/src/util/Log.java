package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Log {

  File file;

  public Log() {
    file = null;
  }

  public void createFile(String s) {
    file = new File(s);
    logOnly("\n\n-------------------------------------------------------------------");
  }

  public void log(String s){
    s = DataUtils.getCurrentTime()+": "+s;
    try {
      FileWriter writer = new FileWriter(file, true);
      System.out.print(s);
      writer.write(s);
      writer.close();
    } catch (IOException e) {
      System.out.println("Could not write on file "+file.getPath());
    }

  }

  public void logln(String s) {
    s = DataUtils.getCurrentTime()+": "+s;
    FileWriter writer = null;
    try {
      writer = new FileWriter(file, true);
      System.out.println(s);
      writer.write(s+"\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Could not write on file "+file.getPath());
    }

  }

  public void logOnly(String s) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(file, true);
      writer.write(s+"\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Could not write on file "+file.getPath());
    }
  }
}
