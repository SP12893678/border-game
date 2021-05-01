package game.gobang.onlinemode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import game.gobang.Gobang;
import listener.DragListener;
import listener.JTextFieldHintListener;

public class CreatRoom extends JDialog {

    PrintStream writer;
    private static final long serialVersionUID = 1L;
    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath + "room-bg.png").getImage();
    private Image icon_close = new ImageIcon(imagepath + "close.png").getImage();
    private Image icon_close_float = new ImageIcon(imagepath + "close-float.png").getImage();
    private JLabel btn_close = new JLabel();
    private JLabel text_roomNum = new JLabel();
    private JTextField text_roomtitle = new JTextField();
    private Image icon_creatroom = new ImageIcon(imagepath + "join.png").getImage();
    private JLabel btn_creatroom = new JLabel();

    public CreatRoom(JFrame owner)
    {
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
    public void Component()
    {
        setContentPane(new JLabel(new ImageIcon(background.getScaledInstance(600, 300, BufferedImage.SCALE_SMOOTH))));
        /*-----------------------------------------------------*/
        btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        btn_close.setBounds(565, 15, 16, 16);
        btn_close.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) 
            {
                btn_close.setIcon(new ImageIcon(icon_close_float.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseExited(MouseEvent e) 
            {
                btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseClicked(MouseEvent e) 
            {
                closeDialog();
            }
        });
        add(btn_close);
        /*-----------------------------------------------------*/
        text_roomNum.setText("請輸入房間標題");
        text_roomNum.setFont(new Font("新細明體", Font.PLAIN, 20));
        text_roomNum.setForeground(Color.black);
        text_roomNum.setHorizontalAlignment(JLabel.CENTER);
        text_roomNum.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        text_roomNum.setBounds(200 ,50, 200, 30);
        add(text_roomNum);
        /*-----------------------------------------------------*/
        /*-----------------------------------------------------*/
        Border bottom = BorderFactory.createMatteBorder(0, 2, 0, 0, Color.black);
        Border empty = new EmptyBorder(0, 15, 0, 0);
        Border border = new CompoundBorder(bottom, empty);
        /*-----------------------------------------------------*/
        text_roomtitle.setFont(new Font("新細明體", Font.PLAIN, 18));
        text_roomtitle.setOpaque(false);
        text_roomtitle.setBorder(border);
        text_roomtitle.setBounds(175, 130, 210, 25);
        add(text_roomtitle);
        /*-----------------------------------------------------*/
        btn_creatroom.setText("建立房間");
        btn_creatroom.setForeground(Color.black);
        btn_creatroom.setIcon(new ImageIcon(icon_creatroom.getScaledInstance(90, 60, BufferedImage.SCALE_SMOOTH)));
        btn_creatroom.setIconTextGap(-75);
        btn_creatroom.setBounds(400, 110, 90, 60);
        btn_creatroom.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                writer.println("Gobang_Creat_Room " + text_roomtitle.getText());
                writer.flush();
            }
        });
        add(btn_creatroom);
    }

    public void setWriter(PrintStream writer) 
    {
        this.writer = writer;
    }
    
    public void showDialog() 
    {
        Gobang.disable = true;
        setVisible(true);    
    }

    public void closeDialog() 
    {
        Gobang.disable = false;
        setVisible(false);
    }
}