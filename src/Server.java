import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ArrayList<String> wordList = new ArrayList<>();
    private static Random random = new Random();

    public static void main(String[] args) {
        int Port = 1234;
        loadWordsFromFile();

        try (ServerSocket serverSocket = new ServerSocket(Port)) {
            System.out.println("Server started on port " + Port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();

                if (clients.size() == 1) {
                    startGame();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadWordsFromFile() {
        try {
            String path = System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "wordlist.txt";
            File file = new File(path);


            if (!file.exists()) {
                wordList.addAll(Arrays.asList("cat", "dog", "pig", "goose", "wolf"));
                return;
            }

            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                wordList.add(sc.next());
            }
            sc.close();
        } catch (Exception e) {
        }
    }


    private static void startGame() {
        new Thread(() -> {
            broadcast("START");
            try {
                while (true) {
                    Thread.sleep(2000);
                    String word = wordList.get(random.nextInt(wordList.size()));
                    int y = 100 + random.nextInt(400);
                    String dir = random.nextBoolean() ? "L" : "R";
                    int x = dir.equals("L") ? 1024 : -50;
                    broadcast("SPAWN " + word + " " + x + " " + y + " " + dir);
                }
            } catch (Exception e) {
                System.out.println("Spawn loop stopped");
            }
        }).start();
    }

    public static void broadcast(String msg) {
        for (ClientHandler c : clients) {
            c.send(msg);
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            try {
                out.println("Connected to server!");
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Client: " + msg);
                    broadcast(msg);
                }
                socket.close();
            } catch (IOException e) {
                System.out.println("Client disconnected");
            }
        }

        void send(String msg) {
            out.println(msg);
        }
    }
}
