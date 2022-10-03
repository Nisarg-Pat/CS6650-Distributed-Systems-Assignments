package client;

import java.io.IOException;

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

  /**
   * Requests a response from the server for an input
   *
   * @param input Request input string
   * @throws IOException
   */
  void request(String input) throws IOException;

  /**
   * Gets the output from the Response.
   * Response is of the form: RESPONSE(<output>)
   *
   * @param response The response string from the server
   * @return The <output> from the response
   */
  String getOutput(String response);

}
