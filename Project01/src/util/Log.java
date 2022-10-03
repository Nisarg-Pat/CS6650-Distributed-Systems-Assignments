package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class to log the requests and responses of client or server to a file
 */
public class Log {

  File file;

  /**
   * Constructor for Log. (Does nothing)
   */
  public Log() {
    file = null;
  }

  /**
   * Creates/opens a log file with given name
   *
   * @param s Name of the file to be created/opened
   */
  public void createFile(String s) {
    file = new File(s);
    logOnly("\n\n-------------------------------------------------------------------");
  }

  /**
   * Logs without \n. Similar to System.out.print(s)
   *
   * @param s String to log
   */
  public void log(String s) {
    s = DataUtils.getCurrentTime() + ": " + s;
    try {
      FileWriter writer = new FileWriter(file, true);
      System.out.print(s);
      writer.write(s);
      writer.close();
    } catch (IOException e) {
      System.out.println("Could not write on file " + file.getPath());
    }

  }

  /**
   * Logs with \n. Similar to System.out.println(s)
   *
   * @param s String to log
   */
  public void logln(String s) {
    s = DataUtils.getCurrentTime() + ": " + s;
    FileWriter writer = null;
    try {
      writer = new FileWriter(file, true);
      System.out.println(s);
      writer.write(s + "\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Could not write on file " + file.getPath());
    }

  }

  /**
   * Only logs into file without printing in console.
   *
   * @param s String to log
   */
  public void logOnly(String s) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(file, true);
      writer.write(s + "\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Could not write on file " + file.getPath());
    }
  }
}
