
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class RightSide extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image background = new ImageIcon("./res/images/server-bg.png").getImage();
    private JLabel bg = new JLabel();
    public JLabel ip;
    public JLabel login_online_count = new JLabel();
    public JLabel user_online_count = new JLabel();
    public JLabel gobang_online_count = new JLabel();
    public JLabel server_start_time = new JLabel();
    public JTextArea logArea;
    public void setBackground()
    {
        bg.setIcon(new ImageIcon(background.getScaledInstance(960, 640, BufferedImage.SCALE_SMOOTH)));
        bg.setBounds(-200, 0, 960, 640);
        add(bg);
    }
}