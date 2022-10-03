package util;

import java.util.Base64;

public class DataUtils {

  public static void validateClientArguments(String[] args) {
    if (args.length != 2) {
      System.out.println("Improper number of arguments! Required 2 arguments: host port");
      System.exit(1);
    }

    try {
      Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println(args[1] + " is not a valid port number!!");
      System.exit(1);
    }
  }

  public static void validateServerArguments(String[] args) {
    if (args.length != 1) {
      System.out.println("Improper number of arguments! Required 1 argument: port");
      System.exit(1);
    }

    try {
      Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.out.println(args[0] + " is not a valid port number!!");
      System.exit(1);
    }
  }

  public static String encode(String input) {
    byte[] bytes = input.getBytes();
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static String decode(String input) {
    byte[] bytes = Base64.getDecoder().decode(input.getBytes());
    return new String(bytes);
  }
}
