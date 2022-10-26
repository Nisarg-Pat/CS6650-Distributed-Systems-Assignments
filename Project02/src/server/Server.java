package server;

import java.io.IOException;
import java.net.InetAddress;

public interface Server {

  /**
   * Starts a server, listening at port based on the protocol it is created.
   *
   * @throws IOException
   */
  void start() throws IOException;

  /**
   * Formats the request string in readable form.
   *
   * @param addr  The InetAddress of the client
   * @param port  The port number of the client
   * @param input The input request from the client
   * @return Request string in readable form
   */
  String createInputString(InetAddress addr, int port, String input);

  /**
   * Formats the client InetAddress and port
   *
   * @param addr The InetAddress of the client
   * @param port The port number of the client
   * @return The client details in String format
   */
  String getAddress(InetAddress addr, int port);

  /**
   * Process the input and returns the response to be sent back to client
   *
   * @param input The client input(request)
   * @return Response String to be sent back to client
   */
  String getOutput(String input);

  /**
   * Processes Request made by client. Possible requests:
   * ALL - Gives the list of all key-value pair in the database
   * PUT <key> <value> - Puts a key-value pair in database
   * GET <key> - Gets the value for the key if present
   * DELETE <key> - Deletes the key
   *
   * @param input Request by client
   * @return Server output of the request
   */
  String processRequest(String input);
}
