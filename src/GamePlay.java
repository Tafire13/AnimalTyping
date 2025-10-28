import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.Timer;

public class GamePlay extends JFrame {
    public GamePlay(Socket socket, BufferedReader in, PrintWriter out) {
        setTitle("Animal Typing - Online");
        setSize(Constant.Width, Constant.Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GamePlayPanel(socket, in, out));
    }
}

class GamePlayPanel extends JPanel implements ActionListener {
    private Image BackGround = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator +  "backGround" + File.separator + "NewVer.png"
    );

    private ArrayList<Image> animalImages = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private Timer timer;
    private Timer countdownTimer;
    private boolean gameStarted = false;
    private int countdown = 5;

    private JTextField fill;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public GamePlayPanel(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;

        setLayout(null);
        loadAnimalImages();
        createTextField();

        new Thread(this::listenServer).start();
    }

    private void startCountdown() {
        countdown = 5;
        countdownTimer = new Timer(1000, e -> {
            repaint();
            countdown--;
            if (countdown < 0) {
                countdownTimer.stop();
                startGame();
            }
        });
        countdownTimer.start();
    }

    private void listenServer() {
        try {
            String msg = in.readLine();
            while (msg != null) {
                System.out.println("From server: " + msg);

                if (msg.equals("START")) {
                    startCountdown();
                } else if (msg.startsWith("SPAWN")) {
                    spawnFromServer(msg);
                }
                msg = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("Lost connection to server.");
        }
    }

    private void startGame() {
        gameStarted = true;
        timer = new Timer(100, this);
        timer.start();
    }

    private void loadAnimalImages() {
        animalImages.add(new ImageIcon("Image/TinyChick.png").getImage());
        animalImages.add(new ImageIcon("Image/HonkingGoose.png").getImage());
        animalImages.add(new ImageIcon("Image/DaintyPig.png").getImage());
        animalImages.add(new ImageIcon("Image/TimberWolf.png").getImage());
    }

    private void createTextField() {
        fill = new JTextField();
        fill.setBounds(350, Constant.Height - 100, 300, 40);
        fill.setHorizontalAlignment(JTextField.CENTER);
        fill.setFont(new Font("Arial", Font.BOLD, 18));
        fill.setForeground(Color.BLACK);
        fill.setOpaque(false);
        fill.setBorder(null);
        fill.setBackground(new Color(0, 0, 0, 0));

        Image typeBar = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "Image" + File.separator + "TextField"+ File.separator + "typingBar.png");

        Image resized = typeBar.getScaledInstance(500, 300, Image.SCALE_SMOOTH);

        JLabel barLabel = new JLabel(new ImageIcon(resized));
        barLabel.setBounds(fill.getX(), fill.getY(), fill.getWidth(), fill.getHeight());
        barLabel.setLayout(new BorderLayout());
        barLabel.setOpaque(false);

        barLabel.add(fill);
        add(barLabel);

        fill.addActionListener(e -> {
            String text = fill.getText().trim().toLowerCase();
            if (!text.isEmpty()) {
                Animal target = null;
                for (Animal a : animals) {
                    if (a.getWord().equalsIgnoreCase(text)) {
                        target = a;
                        break;
                    }
                }
                if (target != null) {
                    animals.remove(target);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "INCORRECT \"" + text + "\"",
                            "INCORRECT",
                            JOptionPane.WARNING_MESSAGE);
                }

                fill.setText("");
            }
        });
    }



    private void spawnFromServer(String msg) {
        String[] data = msg.split(" ");
        String word = data[1];
        int x = Integer.parseInt(data[2]);
        int y = Integer.parseInt(data[3]);
        int speed = data[4].equals("L") ? -3 : 3;
        Image img = animalImages.get(new Random().nextInt(animalImages.size()));
        animals.add(new Animal(img, x, y, speed, word));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted) return;
        for (Animal a : animals) a.update(getWidth());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BackGround, 0, 0, getWidth(), getHeight(), this);

        for (Animal a : animals)
            a.draw(g, this);

        if (!gameStarted) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Arial", Font.BOLD, 80));
            g2.setColor(Color.WHITE);
            String text = (countdown > 0) ? String.valueOf(countdown) : "START!";
            int textWidth = g2.getFontMetrics().stringWidth(text);
            int textHeight = g2.getFontMetrics().getHeight();
            g2.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 100);
        }
    }
}


class Animal {
    private Image image;
    private int x, y, speed;
    private String word;
    private int frame = 0;

    public Animal(Image img, int startX, int startY, int speed, String word) {
        this.image = img;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void update(int width) {
        frame = (frame + 1) % 4;
        x += speed;
        if (x > width + 100) x = -50;
        if (x < -100) x = width + 50;
    }

    public void draw(Graphics g, JPanel panel) {
        Graphics2D g2 = (Graphics2D) g;
        int frameWidth = 16, frameHeight = 16;

        if (speed >= 0) {
            g2.drawImage(image, x, y, x + 32, y + 32,
                    frame * frameWidth, 0, frame * frameWidth + frameWidth, frameHeight, panel);
        } else {
            g2.drawImage(image, x + 32, y, x, y + 32,
                    frame * frameWidth, 0, frame * frameWidth + frameWidth, frameHeight, panel);
        }
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.BLACK);
        g2.drawString(word, x, y - 5);
    }
}
