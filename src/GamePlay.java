import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GamePlay extends JFrame {
    GamePlay() {
        setTitle("Animal Typing - Gameplay");
        setSize(Constant.Width, Constant.Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePlayPanel());
    }

    public static void main(String[] args) {
        new GamePlay().setVisible(true);
    }
}

class GamePlayPanel extends JPanel implements ActionListener {
    private Image BackGround = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image" + File.separator + "BackGround.png"
    );

    private ArrayList<Image> animalImages = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<String> wordList = new ArrayList<>();

    private Timer timer;
    private Timer countdownTimer;
    private int spawnCounter = 0;
    private int spawnInterval = 10;
    private int maxAnimals = 20;
    private int minGrassY = 100;
    private int maxGrassY = 550;
    private int countdown = 5;
    private boolean gameStarted = false;
    private JTextField fill;

    GamePlayPanel() {
        setLayout(null);
        loadWordsFromFile();
        loadAnimalImages();
        createTextField();
        startCountdown();
    }

    private void startCountdown() {
        countdown = 5;
        countdownTimer = new Timer(1000, e -> {
            repaint();
            countdown--;
            if (countdown < 0) {
                ((Timer) e.getSource()).stop();
                gameStarted = true;
                fill.setEnabled(true);
                startGame();
            }
        });
        countdownTimer.start();
    }

    private void startGame() {
        timer = new Timer(100, this);
        timer.start();
    }
    private void loadWordsFromFile() {
        try {
            String path = System.getProperty("user.dir")+ File.separator + "src" + File.separator + "wordlist.txt";
            File file = new File(path);

            if (!file.exists()) {
                throw new Exception("ไม่พบไฟล์ wordlist.txt ที่ " + file.getAbsolutePath());
            }

            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                wordList.add(sc.next());
            }
            sc.close();
        } catch (Exception e) {
        }
    }

    private void loadAnimalImages() {
        try {
            animalImages.add(new ImageIcon("Image/TinyChick.png").getImage());
            animalImages.add(new ImageIcon("Image/HonkingGoose.png").getImage());
            animalImages.add(new ImageIcon("Image/DaintyPig.png").getImage());
            animalImages.add(new ImageIcon("Image/TimberWolf.png").getImage());
        } catch (Exception e) {
        }
    }

    private void createTextField() {
        fill = new JTextField();
        fill.setBounds(350, Constant.Height - 100, 300, 40);
        fill.setFont(new Font("Arial", Font.BOLD, 18));
        fill.setForeground(Color.BLACK);
        fill.setBackground(new Color(255, 255, 255, 200));
        add(fill);
    }

    private void spawnAnimal() {
        if (animals.size() >= maxAnimals) return;
        if (wordList.isEmpty() || animalImages.isEmpty()) return;

        boolean fromLeft = new Random().nextBoolean();
        Image img = animalImages.get(new Random().nextInt(animalImages.size()));

        int x, speed;
        int y = minGrassY + new Random().nextInt(maxGrassY - minGrassY);
        String word = wordList.get(new Random().nextInt(wordList.size()));

        if (fromLeft) {
            x = -50;
            speed = 2 + new Random().nextInt(3);
        } else {
            x = Constant.Width + 50;
            speed = -(2 + new Random().nextInt(3));
        }

        animals.add(new Animal(img, x, y, speed, word));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BackGround, 0, 0, getWidth(), getHeight(), this);

        for (Animal a : animals) {
            a.draw(g, this);
        }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        spawnCounter++;

        if (spawnCounter >= spawnInterval) {
            spawnCounter = 0;
            spawnAnimal();
        }

        for (Animal a : animals) {
            a.update(getWidth());
        }

        repaint();
    }
}

class Animal {
    private Image image;
    private int frameWidth = 16;
    private int frameHeight = 16;
    private int frameCount = 4;
    private int currentFrame = 0;

    private int x, y;
    private int speed;
    private String word;

    public Animal(Image img, int startX, int startY, int speed, String word) {
        this.image = img;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.word = word;
    }

    public void update(int panelWidth) {
        currentFrame = (currentFrame + 1) % frameCount;
        x += speed;

        if (speed > 0 && x > panelWidth + 100) x = -50;
        if (speed < 0 && x < -100) x = panelWidth + 50;
    }

    public void draw(Graphics g, JPanel panel) {
        int sx = currentFrame * frameWidth;
        int sy = 0;
        Graphics2D g2 = (Graphics2D) g;

        if (speed >= 0)
            g2.drawImage(image, x, y, x + frameWidth * 2, y + frameHeight * 2,
                    sx, sy, sx + frameWidth, sy + frameHeight, panel);
        else
            g2.drawImage(image, x + frameWidth * 2, y, x, y + frameHeight * 2,
                    sx, sy, sx + frameWidth, sy + frameHeight, panel);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.BLACK);
        g2.drawString(word, x + 2, y - 6);
        g2.setColor(Color.WHITE);
        g2.drawString(word, x + 1, y - 7);
    }
}
