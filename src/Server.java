import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int Port = 1234;
        try {
            ServerSocket serverSocket = new ServerSocket(Port);
            while (true){
                Socket server = serverSocket.accept();
                System.out.println("OPEN SEVER");
                BufferedReader ServerRead = new BufferedReader(new InputStreamReader(server.getInputStream()));
                PrintWriter ServerOutPut = new PrintWriter(server.getOutputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
