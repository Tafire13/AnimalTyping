import java.net.Socket;

public class Player {
    private String name;
    private int id;
    private Socket socket;

    public Player(String name, int id, Socket socket) {
        this.name = name;
        this.id = id;
        this.socket = socket;
    }

    public void setName(String name) {this.name = name;}
    public String getName() { return name; }
    public int getId() { return id; }
    public Socket getSocket() { return socket; }
}
