import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 1234);
            BufferedReader ClientRead = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter ClientOutPut = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
