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
    int minGrassY = 180;
    int maxGrassY = 550;

    GamePlayPanel() {
        setLayout(null);
        animalImages.add(new ImageIcon("Image/TinyChick.png").getImage());
        animalImages.add(new ImageIcon("Image/HonkingGoose.png").getImage());
        animalImages.add(new ImageIcon("Image/DaintyPig.png").getImage());
        animalImages.add(new ImageIcon("Image/TimberWolf.png").getImage());

        for (int i = 0; i < 10; i++) {
            boolean fromLeft = new Random().nextBoolean();
            Image img = animalImages.get(new Random().nextInt(animalImages.size()));
            int x;
            int speed;
            int y = minGrassY + new Random().nextInt(maxGrassY - minGrassY);
            if (fromLeft) {
                x = new Random().nextInt(100);
                speed = 2 + new Random().nextInt(3);
            } else {

                x = Constant.Width - 100 + new Random().nextInt(50);
                speed = -(2 + new Random().nextInt(3));
            }
            animals.add(new Animal(img, x, y, speed));

        }

        timer = new Timer(100, this);
        timer.start();
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
        for (Animal a : animals) {
            a.update(getWidth());
        }
        repaint();
    }
}
class Animal{
    private Image image;
    private int frameWidth = 16;
    private int frameHeight = 16;
    private int frameCount = 4;
    private int currentFrame = 0;

    private int x, y;
    private int speed;
    private Random rand = new Random();

    public Animal(Image img, int startX, int startY, int speed) {
        this.image = img;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
    }

    public void update(int panelWidth) {
        currentFrame = (currentFrame + 1) % frameCount;
        x += speed;
        if (x > panelWidth) x = -frameWidth;
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
    public int getY() {
        return y;
    }
}
