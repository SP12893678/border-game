package game.gobang;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BoardJPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private String imagepath = "./res/images/";
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon borad = new ImageIcon(imagepath + "board.png");
        g.drawImage(borad.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public int setPosition(double num) {
        return (int) ((num - this.getWidth() * 0.0397f) / (this.getWidth() * 0.06496f) + 0.5f);
    }

    public void addPiece(int y, int x, int player) {
        new Thread() {
            public void run() {
                ImageIcon white = new ImageIcon(imagepath + "white.png");
                ImageIcon black = new ImageIcon(imagepath + "black.png");

                Graphics g = getGraphics();
                Graphics2D g2d = (Graphics2D) g;
				
				int width = getWidth();
				System.out.println(width + "");
                int showX = (int) (x * width * 0.0655f + width * 0.015f);
                int showY = (int) (y * width * 0.0655f + width * 0.015f);
                
                setDoubleBuffered(true);

                if (player == 1)
                    g2d.drawImage(black.getImage(), showX, showY, 28, 28, null);
                else
                    g2d.drawImage(white.getImage(), showX, showY, 28, 28, null);
            }
        }.start();
    }
}