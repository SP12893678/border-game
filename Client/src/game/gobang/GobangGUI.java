package game.gobang;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GobangGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int screenWidth = (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
    private static final int screenHeight = (java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
    private static final int windowX = screenWidth / 2;
    private static final int windowY = screenHeight / 2;

    // private static int[][] board = new int[15][15];
    // private static int i = -1;
    private static int count;
    private BoardJPanel boardJPanel = null;

    private String user;
    PrintStream writer;

    public GobangGUI() {
        setTitle("GOBANG");
        // setUndecorated(true);
        setContentPane(getBoardPanel());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem restart = new JMenuItem("New Game");
        menu.add(restart);
        restart.addActionListener(e -> {
            //restartGame();
        });
        menuBar.add(menu);
        setJMenuBar(menuBar);

        pack();
        setLocation(windowX - this.getWidth() / 2, windowY - this.getHeight() / 2);
        System.out.println(this.getWidth() + " " + this.getHeight());
        
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public void setWriter(PrintStream writer) {
        this.writer = writer;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BoardJPanel getBoardPanel() {
        if (boardJPanel == null) {
            boardJPanel = new BoardJPanel();
            boardJPanel.setPreferredSize(new Dimension(508, 508));
            boardJPanel.setLayout(null);

            boardJPanel.requestFocus();
            boardJPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new Thread() {
                        public void run() {
                            double mouseX = boardJPanel.getMousePosition().getX();
                            double mouseY = boardJPanel.getMousePosition().getY();

                            int pointX = boardJPanel.setPosition(mouseX);
                            int pointY = boardJPanel.setPosition(mouseY);

                            writer.println("Point " + user + "|" + pointX + "|" + pointY);
                            writer.flush();

                            /*if (board[pointX][pointY] == 0) {
                                i = -i;
                                board[pointX][pointY] = i;
                                boardJPanel.addPiece(pointY, pointX, i);
                                if (isWin(pointX, pointY, i)) {
                                    String msg = i == 1 ? "Black Win!!" : "White Win!!";
                                    JOptionPane.showMessageDialog(null, msg);
                                    restartGame();
                                }
                            }*/
                        }
                    }.start();
                }
            });
        }
        return boardJPanel;
    }

    /*private boolean isWin(int x, int y, int player) {
        int tempX = x;
        int tempY = y;
        int checkX[] = { -1, 1, 1, -1, 1, -1, 0, 0 };
        int checkY[] = { -1, 1, -1, 1, 0, 0, 1, -1 };
        int direction = 0;
        int count = 1;

        while (direction < 8 && count < 5) {
            try {
                tempX += checkX[direction];
                tempY += checkY[direction];
                if (board[tempX][tempY] == player)
                    count++;
                else {
                    throw new Exception();
                }
            } catch (Exception e) {
                if (direction % 2 == 1)
                    count = 1;
                tempX = x;
                tempY = y;
                direction++;
            }
        }

        if (count >= 5) {
            return true;
        }

        return false;
    }

    private void restartGame() {
        i = -1;
        board = new int[15][15];
        repaint();
    }*/
}