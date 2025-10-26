import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
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
class HostPanel extends JPanel {
    private Image bg = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir") + File.separator + "Image"+ File.separator + "backGround" + File.separator + "bg2.png");
    private Image board = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir")+ File.separator + "Image"+ File.separator + "TextField" + File.separator + "boardPlayer.png");
    private Image BackButton = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir")+ File.separator + "Image"+ File.separator + "botton" + File.separator + "backButton.png");
    private Image namePlayer = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir")+ File.separator + "Image"+ File.separator + "TextField" + File.separator + "namePlayer.png");
    private Image Start1Min = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir")+ File.separator + "Image"+ File.separator + "botton" + File.separator + "1min.png");
    private Image Start3Min = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir")+ File.separator + "Image"+ File.separator + "botton" + File.separator + "3min.png");
    private Image Start5Min = Toolkit.getDefaultToolkit().createImage(
            System.getProperty("user.dir")+ File.separator + "Image"+ File.separator + "botton" + File.separator + "5min.png");

    HostPanel(JFrame frame) throws IOException {
        setLayout(null);

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
        OneMin.addActionListener(e -> {
            System.out.println("1MIN");
        });
        add(OneMin);

        Image resized3min = Start3Min.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JButton ThreeMin = new JButton(new ImageIcon(resized3min));
        ThreeMin.setFont(new Font("Pixel", Font.BOLD, 20));
        ThreeMin.setBounds(600, 600, 150, 100);
        ThreeMin.setBorderPainted(false);
        ThreeMin.setContentAreaFilled(false);
        ThreeMin.setFocusPainted(false);
        ThreeMin.setOpaque(false);
        ThreeMin.addActionListener(e -> {
            System.out.println("3MIN");
        });
        add(ThreeMin);

        Image resized5min = Start5Min.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JButton FiveMin = new JButton(new ImageIcon(resized5min));
        FiveMin.setFont(new Font("Pixel", Font.BOLD, 20));
        FiveMin.setBounds(800, 600, 150, 100);
        FiveMin.setBorderPainted(false);
        FiveMin.setContentAreaFilled(false);
        FiveMin.setFocusPainted(false);
        FiveMin.setOpaque(false);
        FiveMin.addActionListener(e -> {
            System.out.println("5MIN");
        });
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
            } catch (IOException ex) { throw new RuntimeException(ex); }
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

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}

class JoinPanel extends JPanel {
    private Image bg;

    JoinPanel(JFrame frame) throws IOException {
        setLayout(null);
        bg = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir") + File.separator + "Image" + File.separator + "join_bg.png");

        JLabel playerBox = new JLabel("PLAYER");
        playerBox.setFont(new Font("Pixel", Font.BOLD, 24));
        playerBox.setForeground(Color.WHITE);
        playerBox.setBounds(80, 100, 200, 400);
        add(playerBox);

        JLabel codeLabel = new JLabel("CODE : ");
        codeLabel.setFont(new Font("Pixel", Font.PLAIN, 22));
        codeLabel.setForeground(Color.YELLOW);
        codeLabel.setBounds(100, 520, 100, 40);
        add(codeLabel);

        JTextField codeField = new JTextField();
        codeField.setBounds(200, 520, 150, 40);
        codeField.setHorizontalAlignment(JTextField.CENTER);
        add(codeField);

        JButton joinBtn = new JButton("JOIN");
        joinBtn.setFont(new Font("Pixel", Font.BOLD, 20));
        joinBtn.setBounds(820, 600, 150, 50);
        add(joinBtn);

        JButton back = new JButton("BACK");
        back.setBounds(20, 20, 100, 40);
        back.addActionListener(e -> {
            try {
                frame.setContentPane(new MenuPanel(frame));
                frame.revalidate();
            } catch (IOException ex) { throw new RuntimeException(ex); }
        });
        add(back);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}
