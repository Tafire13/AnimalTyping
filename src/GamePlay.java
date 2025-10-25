import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GamePlay extends JFrame {
    GamePlay() {
        setSize(Constant.Width, Constant.Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GamePlayPanel());
    }

    public static void main(String[] args) {
        new GamePlay().setVisible(true);
    }
}

class GamePlayPanel extends JPanel implements ActionListener {
    Image BackGround = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir")
            + File.separator + "Image" + File.separator + "BackGround.png");
    private ArrayList<Image> animalImages = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();

    private Timer timer;
    private int spawnCounter = 0;
    private int spawnInterval = 10;
    private int maxAnimals = 20;
    
    int minGrassY = 100;
    int maxGrassY = 550;

    JTextField fill;

    GamePlayPanel() {
        setLayout(null);

        // โหลดรูปสัตว์
        animalImages.add(new ImageIcon("Image/TinyChick.png").getImage());
        animalImages.add(new ImageIcon("Image/HonkingGoose.png").getImage());
        animalImages.add(new ImageIcon("Image/DaintyPig.png").getImage());
        animalImages.add(new ImageIcon("Image/TimberWolf.png").getImage());

        // ใส่ช่องพิมพ์
        fill = new JTextField();
        fill.setBounds(350, Constant.Height - 100, 300, 40);
        fill.setFont(new Font("Arial", Font.BOLD, 18));
        fill.setForeground(Color.BLACK);
        fill.setBackground(new Color(255, 255, 255, 200));
        add(fill);

        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnAnimal() {
        if (animals.size() >= maxAnimals) return;

        boolean fromLeft = new Random().nextBoolean();
        Image img = animalImages.get(new Random().nextInt(animalImages.size()));

        int x;
        int speed;
        int y = minGrassY + new Random().nextInt(maxGrassY - minGrassY);

        if (fromLeft) {
            x = -50;
            speed = 2 + new Random().nextInt(3);
        } else {
            x = Constant.Width + 50; // เริ่มนอกจอขวา
            speed = -(2 + new Random().nextInt(3));
        }

        animals.add(new Animal(img, x, y, speed));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BackGround, 0, 0, getWidth(), getHeight(), this);

        for (Animal a : animals) {
            a.draw(g, this);
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

    public Animal(Image img, int startX, int startY, int speed) {
        this.image = img;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
    }

    public void update(int panelWidth) {
        currentFrame = (currentFrame + 1) % frameCount;
        x += speed;

        // ถ้าออกนอกจอไปอีกฝั่ง ให้ลบออก (หรือวนกลับ)
        if (speed > 0 && x > panelWidth + 100) x = -50;
        if (speed < 0 && x < -100) x = panelWidth + 50;
    }

    public void draw(Graphics g, JPanel panel) {
        int sx = currentFrame * frameWidth;
        int sy = 0;
        Graphics2D g2 = (Graphics2D) g;

        if (speed >= 0) {
            g2.drawImage(image,
                    x, y, x + frameWidth * 2, y + frameHeight * 2,
                    sx, sy, sx + frameWidth, sy + frameHeight,
                    panel);
        } else {
            g2.drawImage(image,
                    x + frameWidth * 2, y, x, y + frameHeight * 2,
                    sx, sy, sx + frameWidth, sy + frameHeight,
                    panel);
        }
    }
}
