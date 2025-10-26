import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuFrame extends JFrame {
    MenuFrame() throws IOException {
        setTitle("Animal Typing");
        setSize(Constant.Width, Constant.Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new MenuPanel());
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

    MenuPanel() throws IOException {
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
            System.out.println("Host");
        });

        repaint();

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
            System.out.println("Join");
        });

        repaint();

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