package client;

import java.io.IOException;

/**
 * Interface for Client
 */
public interface Client {

  /**
   * Starts the interactive client. Requests are typed in Command Line. Following are the types of requests:
   * PUT <key> <value> - Puts a key-value pair
   * GET <key> - Gets the value for the key if present
   * DELETE <key> - Deletes the key
   * QUIT - Closes the client
   *
   * @throws IOException
   */
  void execute() throws IOException;

}
