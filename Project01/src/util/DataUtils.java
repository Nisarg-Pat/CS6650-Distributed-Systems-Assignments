package util;

import java.util.Base64;

public class DataUtils {
  public static String encode(String input) {
    byte[] bytes = input.getBytes();
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static String decode(String input) {
    byte[] bytes = Base64.getDecoder().decode(input.getBytes());
    return new String(bytes);
  }
}
