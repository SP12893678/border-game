package game.gobang.onlinemode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import javax.swing.*;

import game.gobang.Gobang;
import listener.DragListener;

public class JoinDialog extends JDialog {

    PrintStream writer;
    private static final long serialVersionUID = 1L;
    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath + "room-bg.png").getImage();
    private Image icon_close = new ImageIcon(imagepath + "close.png").getImage();
    private Image icon_close_float = new ImageIcon(imagepath + "close-float.png").getImage();
    private JLabel btn_close = new JLabel();
    private JLabel error_message = new JLabel();
    public RoomListPanel roomlist = new RoomListPanel();
    public JScrollPane listScroll = new JScrollPane();

    public JoinDialog(JFrame owner) {
        super(owner, false);
        setVisible(false);
        setUndecorated(true);
        setSize(832, 598);
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
        setContentPane(new JLabel(new ImageIcon(background.getScaledInstance(832, 598, BufferedImage.SCALE_SMOOTH))));
        /*-----------------------------------------------------*/
        btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        btn_close.setBounds(808, 8, 16, 16);
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
        JScrollBar vertical = listScroll.getVerticalScrollBar();
        vertical.setPreferredSize(new Dimension(0, 0));
        /*-----------------------------------------------------*/
        listScroll.setBounds(0, 0, 832, 598);
        listScroll.setViewportView(roomlist);
        //listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScroll.getViewport().setOpaque(false);
        listScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        listScroll.getVerticalScrollBar().setUnitIncrement(10);
        listScroll.setOpaque(false);
        listScroll.setVerticalScrollBar(vertical);
        listScroll.setBorder(null);
        add(listScroll);
        /*-----------------------------------------------------*/
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

    public void setWriter(PrintStream writer) 
    {
        this.writer = writer;
    }

    public void joinroom(String room_num) 
    {
        error_message.setVisible(false);
        writer.println("Gobang_Join_Room " + room_num);
        writer.flush();
    }

    public void error_message(String message) 
    {
        error_message.setText(message);
        error_message.setVisible(true);
    }

    public class RoomListPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        Image background = new ImageIcon("../images/listframe.png").getImage();
        JLabel listbg;
        JLabel roomNum;
        JLabel roomTitle;
        JLabel roomMaster;
        JLabel joinroom;
        String pos_x,pos_y;

        public RoomListPanel() {
            setLayout(null);
            setPreferredSize(new Dimension(832, 1622));
            setBackground(new Color(0, 0, 0, 0));
        }

        public void creatRoomList(int times,String Num, String Title, String Owner) 
        {
            listbg = new JLabel(new ImageIcon(background.getScaledInstance(783, 78, BufferedImage.SCALE_SMOOTH)));
            roomNum = new JLabel(Num);
            roomTitle = new JLabel(Title);
            roomMaster = new JLabel(Owner);
            joinroom = new JLabel("加入");
            roomNum.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 20));
            roomTitle.setFont(new Font("新細明體", Font.PLAIN, 20));
            roomMaster.setFont(new Font("新細明體", Font.PLAIN, 20));
            joinroom.setFont(new Font("新細明體", Font.PLAIN, 20));
            roomNum.setForeground(Color.white);
            roomTitle.setForeground(Color.white);
            roomMaster.setForeground(Color.white);
            joinroom.setForeground(Color.white);
            int offset = (times-1) * 78;
            listbg.setBounds(30, 30 + offset, 783, 78);
            roomNum.setBounds(80, 55 + offset, 30, 30);
            roomTitle.setBounds(160, 55 + offset, 300, 30);
            roomMaster.setBounds(520, 55 + offset, 80, 30);
            joinroom.setBounds(725, 55 + offset, 100, 30);
            joinroom.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) 
                {
                    joinroom(Num);
                    //System.out.println(Num);
                }
            });
            add(roomNum);
            add(roomTitle);
            add(roomMaster);
            add(joinroom);
            add(listbg);
        }
    }
}