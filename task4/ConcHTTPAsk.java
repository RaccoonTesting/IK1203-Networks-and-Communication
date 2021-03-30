import java.net.*;
import java.io.*;

public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {

        int port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.print("There was no port number input!");
        }

        try {
            //create serversocket
            ServerSocket HTTPSocket = new ServerSocket(port);

            while(true)
            {
                Socket connection = HTTPSocket.accept();
                //Creating MyRunnable object
                MyRunnable r = new MyRunnable(connection);
                //New thread for each request
                new Thread(r).start();
            }
        }
        catch(IOException error) {
            System.err.println(error);
        }
    }
}
