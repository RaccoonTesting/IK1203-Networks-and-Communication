import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

    public class MyRunnable implements Runnable {
        static int BUFFERSIZE = 1024;
        static int MAXSIZE = 10000;

        Socket connection;

        public MyRunnable(Socket connection) {
            this.connection = connection;
        }

        public void run()
        {
            String HTTP404 = "HTTP/1.1 404 Not Found\r\n\r\n";
            String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
            String HTTP400 = "HTTP/1.1 400 Bad Request\r\n\r\n";

            try {


                while (true) {

                    StringBuilder message = new StringBuilder();
                    StringBuilder answer = new StringBuilder();
                    byte[] fromClientBuffer = new byte[BUFFERSIZE];
                    int fromUserLength;
                    boolean statusLineCheck = true;
                    String hostname = null;
                    String inputString = null;
                    String clientAnswer = null;
                    String statusLine = null;
                    int PORT = -1;

                    // read encoded string to buffer end loop if server closes connection max size of message is hit
                    while (connection.getInputStream().available() > 0 && message.length() < MAXSIZE) {

                        fromUserLength = connection.getInputStream().read(fromClientBuffer);

                        //decode string
                        message.append(new String(fromClientBuffer, 0, fromUserLength, StandardCharsets.UTF_8));

                        //splitting the URI into parts
                        if (statusLineCheck) {
                            String uri = message.toString();
                            String[] split_URI = uri.split(" |=|&");
                            //depends on  how many arguments we have host, port, string or just host and port
                            int split_URI_len = split_URI.length;


                            if (split_URI[1].matches("(?i).*/favicon.ico.*") || !(split_URI[1].matches(("(?i).*/ask?.*")))) {
                                statusLine = HTTP404;
                            }
                            //if first element in split_URI (a string array) is the word GET or the second element matches to /ask?
                            else if ((split_URI[0].equals("GET")) && (split_URI[1].matches(("(?i).*/ask?.*")))) {
                                statusLine = HTTP200;
                            } else {
                                statusLine = HTTP400;
                            }

                            hostname = split_URI[2];
                            PORT = Integer.parseInt(split_URI[4]);

                            //If the len is longer then 6 we have a string argument
                            if (split_URI_len > 6) {
                                inputString = split_URI[6];
                                System.out.println(inputString);
                            }
                            statusLineCheck = false;
                        }
                    }

                    try {
                        //with string
                        if (inputString != null)
                            clientAnswer = TCPClient.askServer(hostname, PORT, inputString);
                            //without string
                        else
                            clientAnswer = TCPClient.askServer(hostname, PORT);

                    } catch (IOException error) {
                        System.err.println(error);
                    }

                    //adding status line and client answer to string builder
                    answer.append(statusLine);
                    answer.append(clientAnswer).append("\r\n");

                    String reply = answer.toString();
                    System.out.println(reply);

                    //encode message
                    byte[] to_client = reply.getBytes(StandardCharsets.UTF_8);

                    //write back to client
                    connection.getOutputStream().write(to_client);

                    // close connection
                    connection.close();

                }
            } catch (Exception e) {
                System.out.println("Nop sorry error!  ");
            }
        }
    }
