import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho {
    static int BUFFERSIZE=1024;
    static int MAXSIZE=BUFFERSIZE*BUFFERSIZE;

    public static void main(String[] args) throws IOException {
        int port_num = -1;

        try {
            port_num = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.print("There was no port number input!");
        }

        try {
            //Can only create socket if port nummber is positive
            ServerSocket server = new ServerSocket(port_num);

            while (true) {
                //accepting a new socket connection
                Socket connectionSocket = server.accept();

                System.out.print("New connection");

                StringBuilder sb = new StringBuilder();
                byte[] fromClientBuffer = new byte[BUFFERSIZE];

                int fromUserLength;

                // read encoded string to buffer end loop if server closes connection max size of message is hit
                while ( connectionSocket.getInputStream().available() > 0 && sb.length() < MAXSIZE) {

                    fromUserLength = connectionSocket.getInputStream().read(fromClientBuffer);
                    System.out.println(fromUserLength);

                    System.out.println("Read line");

                    //decode string
                    sb.append(new String(fromClientBuffer, 0, fromUserLength, StandardCharsets.UTF_8));

                }
                System.out.println("yooy connection");
                System.out.print(sb.toString());
                System.out.println("Ready to send...");

                //string message to send back to client
                String message = ("HTTP/1.1 200 OK\n\n" + sb.toString());

                //encode message
                byte[] toClientBuffer = message.getBytes(StandardCharsets.UTF_8);

                //write back to client
                connectionSocket.getOutputStream().write(toClientBuffer);
                System.out.println("Message sent...");

                // close connection
                connectionSocket.close();
                System.out.println("Close connection");

            }
        }
        catch(Exception e){
            System.out.print("Un able to open server socket!");
        }
    }
}
