
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import listener.*;

public class Console extends JFrame {

    private static final long serialVersionUID = 1L;
    private int menuitem_select = 1;
    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath+"server-bg.png").getImage();
    private JLabel panel_menu = new JLabel();
    private JLabel menuitem[] = new JLabel[6];
    RightSide right_panel[] = new RightSide[6];
    

    public Console()
    {
        setVisible(false);
        setFocusable(true);
        setUndecorated(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(imagepath + "server.png"));
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setSize(960, 640);
        setLocationRelativeTo(null);
        DragListener drag = new DragListener();
        addMouseListener(drag);
        addMouseMotionListener(drag);
        setContentPane(new JLabel(new ImageIcon(background.getScaledInstance(960, 640, BufferedImage.SCALE_SMOOTH))));
        Component();
    }
    public void Component()
    {
        
        /*-----------------------------------------------------*/
        setMenuitem(1, "Home");
        setMenuitem(2, "Management");
        setMenuitem(3, "Console log");
        setMenuitem(4, "Setting");
        setMenuitem(5, "Exit");
        /*-----------------------------------------------------*/
        right_panel[1] = new Index();
        right_panel[2] = new RightSide();
        right_panel[3] = new Log();
        right_panel[4] = new RightSide();
        right_panel[5] = new Shutdown();
        /*-----------------------------------------------------*/
        setRightPanel(1);
        setRightPanel(2);
        setRightPanel(3);
        setRightPanel(4);
        setRightPanel(5);
        /*-----------------------------------------------------*/
        panel_menu.setOpaque(true);
        panel_menu.setBackground(new Color(0, 0, 0, 153));
        panel_menu.setBounds(0, 0, 200, 640);
        add(panel_menu);
        /*-----------------------------------------------------*/
    }
    public void showPlatform()
    {
        setVisible(true);
    }

    public void setMenuitem(int order,String title) 
    {
        menuitem[order] = new JLabel();
        menuitem[order].setText(title);
        menuitem[order].setFont(new Font("Fira Code Light", Font.PLAIN, 24));
        menuitem[order].setForeground(Color.white);
        menuitem[order].setHorizontalAlignment(JLabel.CENTER);
        menuitem[order].setBounds(0, order*75, 200, 75);
        if(menuitem_select == order)
        {
            menuitem[order].setBackground(new Color(0, 0, 0, 102));
            menuitem[order].setOpaque(true);
        }
        else
        {
            menuitem[order].setBackground(new Color(0, 0, 0, 51));
        }
        menuitem[order].addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) 
            {
                if(menuitem_select != order)
                {
                    menuitem[order].setOpaque(true);
                    menuitem[order].repaint();
                }
            }
            public void mouseExited(MouseEvent e) 
            {
                if(menuitem_select != order)
                {
                    menuitem[order].setOpaque(false);
                    menuitem[order].repaint();
                }
            }
            public void mouseClicked(MouseEvent e) 
            {
                if(order == 5)
                    System.exit(0);
                refresh(order);
                menuitem_select = order;
                requestFocus();
            }
        });
        add(menuitem[order]);
    }

    private void refresh(int order) 
    {
        right_panel[menuitem_select].setVisible(false);
        menuitem[menuitem_select].setBackground(new Color(0, 0, 0, 51));
        menuitem[menuitem_select].setOpaque(false);
        right_panel[order].setVisible(true);
        menuitem[order].setBackground(new Color(0, 0, 0, 102));
        menuitem[order].setOpaque(true);
        repaint();
    }

    public void setRightPanel(int order) 
    {
        right_panel[order].setBackground(new Color(0, 0, 0, 0));
        if(menuitem_select == order)
            right_panel[order].setVisible(true);
        else
            right_panel[order].setVisible(false);
        right_panel[order].setBounds(200, 0, 760, 640);
        add(right_panel[order]);
    }

    public void logPrint(String log) 
    {
        right_panel[3].logArea.append(log+"\n");
        repaint();
    }

    public void setIP(String ip) 
    {
        right_panel[1].ip.setText("IP → " + ip);
    }

    public void setServerStartTime(String time) 
    {
        right_panel[1].server_start_time.setText("Build Date → " + time);
    }

    public void setLoginPageCount(int count) 
    {
        right_panel[1].login_online_count.setText("Login Page → " + String.valueOf(count));
    }

    public void setUserOnlineCount(int count) 
    {
        right_panel[1].user_online_count.setText("User Online → " + String.valueOf(count));
    }

    public void setGobangOnlineCount(int count) 
    {
        right_panel[1].gobang_online_count.setText("Gobang Online → " + String.valueOf(count));
    }
    // private enum Menuitem {
    //     Home, Management, Console log, Setting, Exit;
    // }
}