import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MenuFrame extends JFrame {
    MenuFrame() throws IOException {
        setTitle("Animal Typing");
        setSize(Constant.Width, Constant.Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new MenuPanel(this));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MenuFrame().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

class MenuPanel extends JPanel {
    private Image bg;
    private JLabel logoLabel;
    private JButton hostButton, joinButton;

    MenuPanel(JFrame frame) throws IOException {
        bg = Toolkit.getDefaultToolkit()
                .createImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "bg1.png");
        setLayout(new GridBagLayout());

        Box box = Box.createVerticalBox();
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIcon = new ImageIcon(
                System.getProperty("user.dir") + File.separator + "Image" + File.separator + "gameLogo.png");
        Image logoImg = logoIcon.getImage();

        int newLogoWidth = 275;
        int newLogoHeight = 400;

        Image scaledLogo = logoImg.getScaledInstance(newLogoWidth, newLogoHeight, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledLogo);
        logoLabel = new JLabel(logoIcon);

        String basePath = System.getProperty("user.dir") + File.separator + "Image" + File.separator;
        BufferedImage hostImg = ImageIO.read(new File(basePath + "host.png"));

        double hostScale = 0.7;
        int hostWidth = (int) (hostImg.getWidth() * hostScale);
        int hostHeight = (int) (hostImg.getHeight() * hostScale);

        Image scaledHost = hostImg.getScaledInstance(hostWidth, hostHeight, Image.SCALE_SMOOTH);
        ImageIcon hostIcon = new ImageIcon(scaledHost);
        hostButton = new JButton(hostIcon);
        hostButton.setBorderPainted(false);
        hostButton.setContentAreaFilled(false);
        hostButton.setFocusPainted(false);
        hostButton.setOpaque(false);
        hostButton.addActionListener(e -> {
            try {
                frame.setContentPane(new HostPanel(frame));
                frame.revalidate();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        BufferedImage joinImg = ImageIO.read(new File(basePath + "join.png"));
        double joinScale = 0.7;
        int joinWidth = (int) (joinImg.getWidth() * joinScale);
        int joinHeight = (int) (joinImg.getHeight() * joinScale);

        Image scaledJoin = joinImg.getScaledInstance(joinWidth, joinHeight, Image.SCALE_SMOOTH);
        ImageIcon joinIcon = new ImageIcon(scaledJoin);
        joinButton = new JButton(joinIcon);
        joinButton.setBorderPainted(false);
        joinButton.setContentAreaFilled(false);
        joinButton.setFocusPainted(false);
        joinButton.setOpaque(false);

        joinButton.addActionListener(e -> {
            try {
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                socket.setSoTimeout(1000);
                if (in.ready() && "FULL".equals(in.readLine())) {
                    JOptionPane.showMessageDialog(this,
                            "FULL",
                            "JOIN NOT SUCCESS",
                            JOptionPane.WARNING_MESSAGE);
                    socket.close();
                    return;
                }

                JOptionPane.showMessageDialog(this, "Connected to Host!");
                frame.setContentPane(new JoinPanel(frame, socket));
                frame.revalidate();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "If no Host is open, please have the Host side press the Host button first.",
                        "Host not found",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hostButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(logoLabel);
        box.add(hostButton);
        box.add(joinButton);
        add(box);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}

class HostPanel extends JPanel {
    private Image bg = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "backGround" + File.separator + "bg2.png");
    private Image board = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "TextField" + File.separator + "boardPlayer.png");
    private Image BackButton = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "botton" + File.separator + "backButton.png");
    private Image namePlayer = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "TextField" + File.separator + "namePlayer.png");
    private Image Start1Min = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "botton" + File.separator + "1min.png");
    private Image Start3Min = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "botton" + File.separator + "3min.png");
    private Image Start5Min = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "botton" + File.separator + "5min.png");

    private JLabel playerLabel;
    private static ArrayList<String> players = new ArrayList<>();
    private static ArrayList<Socket> connectedClients = new ArrayList<>();


    HostPanel(JFrame frame) throws IOException {
        setLayout(null);
        new Thread(() -> startServer()).start();

        String playerName = "Player1";
        players.add(playerName);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(10, 100, 800, 600);
        add(layeredPane);

        playerLabel = new JLabel(playerName, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Pixel", Font.BOLD, 28));
        playerLabel.setForeground(Color.YELLOW);
        playerLabel.setBounds(50, 100, 200, 50);
        layeredPane.add(playerLabel, Integer.valueOf(1));

        Image resizedBoard = board.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JLabel playerBox = new JLabel(new ImageIcon(resizedBoard));
        playerBox.setFont(new Font("Pixel", Font.BOLD, 24));
        playerBox.setForeground(Color.WHITE);
        playerBox.setBounds(10, 100, 300, 400);
        add(playerBox);

        JLabel codeLabel = new JLabel("CODE : 1234");
        codeLabel.setFont(new Font("Pixel", Font.BOLD, 32));
        codeLabel.setForeground(Color.YELLOW);
        codeLabel.setBounds(300, 500, 200, 80);
        add(codeLabel);

        Image resized1min = Start1Min.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JButton OneMin = new JButton(new ImageIcon(resized1min));
        OneMin.setFont(new Font("Pixel", Font.BOLD, 20));
        OneMin.setBounds(400, 600, 150, 100);
        OneMin.setBorderPainted(false);
        OneMin.setContentAreaFilled(false);
        OneMin.setFocusPainted(false);
        OneMin.setOpaque(false);
        add(OneMin);

        Image resized3min = Start3Min.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JButton ThreeMin = new JButton(new ImageIcon(resized3min));
        ThreeMin.setFont(new Font("Pixel", Font.BOLD, 20));
        ThreeMin.setBounds(600, 600, 150, 100);
        ThreeMin.setBorderPainted(false);
        ThreeMin.setContentAreaFilled(false);
        ThreeMin.setFocusPainted(false);
        ThreeMin.setOpaque(false);
        add(ThreeMin);

        Image resized5min = Start5Min.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JButton FiveMin = new JButton(new ImageIcon(resized5min));
        FiveMin.setFont(new Font("Pixel", Font.BOLD, 20));
        FiveMin.setBounds(800, 600, 150, 100);
        FiveMin.setBorderPainted(false);
        FiveMin.setContentAreaFilled(false);
        FiveMin.setFocusPainted(false);
        FiveMin.setOpaque(false);
        add(FiveMin);

        Image resizedBackButton = BackButton.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        JButton back = new JButton(new ImageIcon(resizedBackButton));
        back.setBounds(900, 20, 100, 40);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setOpaque(false);
        back.addActionListener(e -> {
            try {
                frame.setContentPane(new MenuPanel(frame));
                frame.revalidate();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(back);

        Image resizedNamePlayer = namePlayer.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        JTextField NameBar = new JTextField();
        NameBar.setBounds(80, 500, 150, 80);
        NameBar.setHorizontalAlignment(JTextField.CENTER);
        NameBar.setFont(new Font("Arial", Font.BOLD, 18));
        NameBar.setForeground(Color.BLACK);
        NameBar.setOpaque(false);
        NameBar.setBorder(null);
        NameBar.setBackground(new Color(0, 0, 0, 0));

        JLabel name = new JLabel(new ImageIcon(resizedNamePlayer));
        name.setBounds(NameBar.getX(), NameBar.getY(), NameBar.getWidth(), NameBar.getHeight());
        name.setLayout(new BorderLayout());
        name.setOpaque(false);
        name.add(NameBar);
        add(name);

        NameBar.addActionListener(e -> {
            String newName = NameBar.getText().trim();
            if (!newName.isEmpty()) {
                playerLabel.setText(newName);
                players.set(0, newName);
                System.out.println("Player1 renamed to: " + newName);
            }
        });
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started on port 1234");
            while (true) {
                Socket socket = serverSocket.accept();

                if (players.size() >= 4) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("FULL");
                    socket.close();
                    System.out.println("Room full - rejected new connection");
                    continue;
                }
                connectedClients.add(socket);
                int playerNum = players.size() + 1;
                String name = "Player" + playerNum;
                players.add(name);
                System.out.println(name + " joined the game.");
                SwingUtilities.invokeLater(() -> addNewPlayer(name));

                broadcastPlayers();
            }
        } catch (IOException e) {
            System.out.println("Server closed or failed to start");
        }
    }
    private void broadcastPlayers() {
        for (String name : players) {
            System.out.println(">> " + name);
        }

        for (Socket socket : connectedClients) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(String.join(",", players));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addNewPlayer(String playerName) {
        int baseX = 50;
        int baseY = 100;
        int gap = 50;

        JLabel newLabel = new JLabel(playerName, SwingConstants.CENTER);
        newLabel.setFont(new Font("Pixel", Font.BOLD, 28));
        newLabel.setForeground(Color.YELLOW);

        int y = baseY + (players.size() - 1) * gap;
        newLabel.setBounds(baseX, y, 200, 50);

        add(newLabel);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}

class JoinPanel extends JPanel {
    private Image bg = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "backGround" + File.separator + "bg2.png");;
    private Image board = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "TextField" + File.separator + "boardPlayer.png");
    private Image BackButton = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "botton" + File.separator + "backButton.png");
    private Image namePlayer = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "TextField" + File.separator + "namePlayer.png");
    private Socket socket;
    private ArrayList<String> playerNames = new ArrayList<>();
    JoinPanel(JFrame frame, Socket socket){
        this.socket = socket;
        setLayout(null);

        new Thread(this::listenFromHost).start();

        Image resizedBoard = board.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JLabel playerBox = new JLabel(new ImageIcon(resizedBoard));
        playerBox.setFont(new Font("Pixel", Font.BOLD, 24));
        playerBox.setForeground(Color.WHITE);
        playerBox.setBounds(10, 100, 300, 400);
        add(playerBox);

        JLabel codeLabel = new JLabel("CODE : 1234");
        codeLabel.setFont(new Font("Pixel", Font.BOLD, 32));
        codeLabel.setForeground(Color.YELLOW);
        codeLabel.setBounds(300, 500, 200, 80);
        add(codeLabel);

        Image resizedNamePlayer = namePlayer.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        JTextField NameBar = new JTextField();
        NameBar.setBounds(80, 500, 150, 80);
        NameBar.setHorizontalAlignment(JTextField.CENTER);
        NameBar.setFont(new Font("Arial", Font.BOLD, 18));
        NameBar.setForeground(Color.BLACK);
        NameBar.setOpaque(false);
        NameBar.setBorder(null);
        NameBar.setBackground(new Color(0, 0, 0, 0));

        JLabel name = new JLabel(new ImageIcon(resizedNamePlayer));
        name.setBounds(NameBar.getX(), NameBar.getY(), NameBar.getWidth(), NameBar.getHeight());
        name.setLayout(new BorderLayout());
        name.setOpaque(false);
        name.add(NameBar);
        add(name);

        Image resizedBackButton = BackButton.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        JButton back = new JButton(new ImageIcon(resizedBackButton));
        back.setBounds(900, 20, 100, 40);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setOpaque(false);
        back.addActionListener(e -> {
            try {
                frame.setContentPane(new MenuPanel(frame));
                frame.revalidate();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        add(back);
    }
    private void listenFromHost() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String msg;
            while ((msg = in.readLine()) != null) {
                playerNames.clear();
                for (String n : msg.split(",")) playerNames.add(n.trim());
                SwingUtilities.invokeLater(this::updatePlayerList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updatePlayerList() {
        removeOldLabels();
        int y = 100;
        for (String name : playerNames) {
            JLabel lbl = new JLabel(name, SwingConstants.CENTER);
            lbl.setFont(new Font("Pixel", Font.BOLD, 28));
            lbl.setForeground(Color.YELLOW);
            lbl.setBounds(50, y, 200, 50);
            add(lbl);
            y += 50;
        }
        repaint();
    }
    private void removeOldLabels() {
        Component[] components = getComponents();
        ArrayList<Component> toRemove = new ArrayList<>();
        for (Component c : components) {
            if (c instanceof JLabel lbl && lbl.getFont().getSize() == 28) { // font 28 ของ player
                toRemove.add(c);
            }
        }
        for (Component c : toRemove) {
            remove(c);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}
