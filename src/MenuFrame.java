import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuFrame extends JFrame {
    MenuFrame(){
        setSize(Constant.Width, Constant.Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new MenuPanel());
    }

    public static void main(String[] args) {
        new MenuFrame().setVisible(true);
    }
}

class MenuPanel extends JPanel{
        Image BG = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir")
                + File.separator + "Image" + File.separator + "BG.png");
    MenuPanel(){
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BG, 0,0, this);
    }
}
