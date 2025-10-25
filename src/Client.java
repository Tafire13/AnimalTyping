import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Connected to server!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new GamePlay(socket, in, out).setVisible(true);

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
