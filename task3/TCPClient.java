import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class TCPClient {

        // buffer size, message from server
        private static int BUFFERSIZE = 1024;
        // max size off data from server
        private static int MAXSIZE = 2048;
        //time out, if server takes to long to respond
        private static int TIMEOUT = 2000;


        public static String askServer(String hostname, int port, String toServer) throws IOException {

            //encoding string
            byte[] encodedToServer = toServer.getBytes(StandardCharsets.UTF_8);
            //for message from server(encoded)
            byte[] encodedFromServer = new byte[BUFFERSIZE];

            //making socket
            Socket clientSocket = new Socket(hostname, port);
            //making stringbuilder
            StringBuilder fromServer = new StringBuilder();

            //setting timer
            clientSocket.setSoTimeout(TIMEOUT);
            //sending encoded string to server
            clientSocket.getOutputStream().write(encodedToServer, 0, encodedToServer.length);

            try {
                int fromUserLength;
                // read encoded string to buffer end loop is server closes connectionor max size of message is hit
                while ((fromUserLength = clientSocket.getInputStream().read(encodedFromServer)) != -1 && fromServer.length() < MAXSIZE) {
                    //decode string
                    fromServer.append(new String(encodedFromServer, 0, fromUserLength, StandardCharsets.UTF_8));
                }
            }
            //timer runs out
            catch (SocketTimeoutException ste) {
            }
            //close connection
            clientSocket.close();

            //return what we have in the string builder from the server
            return fromServer.toString();
        }

        public static String askServer(String hostname, int port) throws IOException {
            //for data from server, encoded
            byte[] encodedFromServer = new byte[BUFFERSIZE];

            //create socket
            Socket clientSocket = new Socket(hostname, port);
            //create stringbuilder
            StringBuilder fromServer = new StringBuilder();

            //set timer
            clientSocket.setSoTimeout(TIMEOUT);

            try {
                int fromUserLength;
                //read in to buffer quit if server closes connection or max size is hit
                while ((fromUserLength = clientSocket.getInputStream().read(encodedFromServer)) != -1 && fromServer.length() < MAXSIZE) {
                    //decode data from server
                    fromServer.append(new String(encodedFromServer, 0, fromUserLength, StandardCharsets.UTF_8));
                }
            }
            //setting timer
            catch (SocketTimeoutException ste) {
            }

            //close connection
            clientSocket.close();

            //return what we have in the string builder from the server
            return fromServer.toString();
        }
    }
