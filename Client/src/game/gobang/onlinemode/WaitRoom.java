package game.gobang.onlinemode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import game.gobang.Gobang;
import listener.DragListener;

public class WaitRoom extends JDialog {

    PrintStream writer;
    private static final long serialVersionUID = 1L;
    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath + "room-bg.png").getImage();
    private Image icon_close = new ImageIcon(imagepath + "close.png").getImage();
    private Image icon_close_float = new ImageIcon(imagepath + "close-float.png").getImage();
    private JLabel btn_close = new JLabel();
    private JLabel text_roomNum = new JLabel();
    private JLabel text_roomTitle = new JLabel();
    private JLabel text_player1 = new JLabel();
    private JLabel text_player2 = new JLabel();
    private Image icon_startgame = new ImageIcon(imagepath + "join.png").getImage();
    private JLabel btn_startgame = new JLabel();

    public WaitRoom(JFrame owner) {
        super(owner, false);
        setVisible(false);
        setUndecorated(true);
        setSize(600, 300);
        setBackground(new Color(0, 0, 0, 0));
        setResizable(false);
        setLocationRelativeTo(owner);
        setLayout(null);
        DragListener drag = new DragListener();
        addMouseListener(drag);
        addMouseMotionListener(drag);
        Component();
    }

    public void Component() {
        setContentPane(new JLabel(new ImageIcon(background.getScaledInstance(600, 300, BufferedImage.SCALE_SMOOTH))));
        /*-----------------------------------------------------*/
        btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        btn_close.setBounds(565, 15, 16, 16);
        btn_close.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn_close
                        .setIcon(new ImageIcon(icon_close_float.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseExited(MouseEvent e) {
                btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseClicked(MouseEvent e) {
                closeDialog();
            }
        });
        add(btn_close);
        /*-----------------------------------------------------*/
        Border bottom = BorderFactory.createMatteBorder(0, 2, 0, 0, Color.black);
        Border empty = new EmptyBorder(0, 10, 0, 0);
        Border border = new CompoundBorder(bottom, empty);
        /*-----------------------------------------------------*/
        text_roomNum.setText("#房間號碼");
        text_roomNum.setForeground(Color.black);
        text_roomNum.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 20));
        text_roomNum.setBorder(border);
        text_roomNum.setBounds(20, 20, 100, 30);
        add(text_roomNum);
        /*-----------------------------------------------------*/
        text_roomTitle.setText("#房間標題");
        text_roomTitle.setFont(new Font("新細明體", Font.PLAIN, 20));
        text_roomTitle.setForeground(Color.black);
        text_roomTitle.setHorizontalAlignment(JLabel.CENTER);
        text_roomTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        text_roomTitle.setBounds(200, 20, 200, 30);
        add(text_roomTitle);
        /*-----------------------------------------------------*/
        text_player1.setText("#玩家名稱");
        text_player1.setForeground(Color.black);
        text_player1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        text_player1.setBounds(150, 105, 150, 25);
        add(text_player1);
        /*-----------------------------------------------------*/
        text_player2.setText("#玩家名稱");
        text_player2.setForeground(Color.black);
        text_player2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        text_player2.setBounds(150, 135, 150, 25);
        add(text_player2);
        /*-----------------------------------------------------*/
        btn_startgame.setText("開始遊戲");
        btn_startgame.setForeground(Color.black);
        btn_startgame.setIcon(new ImageIcon(icon_startgame.getScaledInstance(90, 60, BufferedImage.SCALE_SMOOTH)));
        btn_startgame.setIconTextGap(-75);
        btn_startgame.setBounds(320, 110, 90, 60);
        btn_startgame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!text_player2.getText().equals("#玩家名稱")) {
                    writer.println("Gobang_online_Start " + text_player1.getText() + "|" + text_player2.getText());
                    writer.flush();
                }
                /* start game */
            }
        });
        add(btn_startgame);
    }

    public void initialize(String num, String title, String player1, String player2) {
        text_roomNum.setText(num);
        text_roomTitle.setText(title);
        text_player1.setText(player1);
        text_player2.setText(player2);
    }

    public void refresh() {
        text_player2.setText("#玩家名稱");
    }

    public void showDialog() {
        Gobang.disable = true;
        setVisible(true);
    }

    public void setWriter(PrintStream writer) {
        this.writer = writer;
    }

    public void closeDialog() {
        Gobang.disable = false;
        setVisible(false);
        writer.println("Gobang_Exit_Room " + text_roomNum.getText());
        writer.flush();
    }

    public void setRoomMaster(boolean is_master) {
        btn_startgame.setVisible(is_master);
    }
}