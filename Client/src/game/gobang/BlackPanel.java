package game.gobang;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class BlackPanel extends JPanel 
{
        private static final long serialVersionUID = 1L;
        Image background = new ImageIcon("./res/images/mask.png").getImage();
        JLabel bg = new JLabel(new ImageIcon(background.getScaledInstance(1260, 680, BufferedImage.SCALE_SMOOTH)));
        public BlackPanel() 
        {
            setLayout(null);
            setPreferredSize(new Dimension(1260, 680));
            setBackground(new Color(0, 0, 0, 0));
            bg.setBounds(0, 0, 1260, 680);
            add(bg);
       }

        public void showPanel() 
        {
            setVisible(true);
        }

        public void closePanel() 
        {
            setVisible(false);
        }
}