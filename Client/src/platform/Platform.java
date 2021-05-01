package platform;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import config.Config;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import transfer.Transfer;
import listener.DragListener;

public class Platform extends JFrame {

    private static final long serialVersionUID = 1L;
    Config config = new Config("./res/config/config.properties");
    TrayIcon trayIcon;
    SystemTray tray;

    boolean connection_status = false;
    Thread readerThread;
    Socket sock;
    String username;
    String name, ip = "";
    BufferedReader reader;
    PrintStream writer;

    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath+"03.png").getImage();
    private Image icon_close = new ImageIcon(imagepath+"close.png").getImage();
    private Image icon_close_float = new ImageIcon(imagepath+"close-float.png").getImage();
    private JLabel btn_close = new JLabel();
    private Image icon_gobang = new ImageIcon(imagepath + "gobang.png").getImage();
    private Image icon_gobang_float = new ImageIcon(imagepath + "gobang-float.png").getImage();
    private JLabel btn_gobang = new JLabel();
    private JLabel text_gobang = new JLabel();
    public Platform(String username)
    {
        this.username = username;
        setVisible(false);
        setFocusable(true);
        setUndecorated(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(imagepath + "07.png"));
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        DragListener drag = new DragListener();
        addMouseListener(drag);
        addMouseMotionListener(drag);
        setTray();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                setState(Frame.ICONIFIED);
            }
        });
    }
    public void Component()
    {
        setContentPane(new JLabel(new ImageIcon(background.getScaledInstance(1080, 720, BufferedImage.SCALE_SMOOTH))));
        /*-----------------------------------------------------*/
        btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        btn_close.setBounds(1058, -3, 32, 32);
        btn_close.addMouseListener(new MouseAdapter(){
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
                close();
            }
        });
        add(btn_close);
        /*-----------------------------------------------------*/

        /*-----------------------------------------------------*/
        btn_gobang.setIcon(new ImageIcon(icon_gobang.getScaledInstance(260, 185, BufferedImage.SCALE_SMOOTH)));
        btn_gobang.setBounds(40, 140, 260, 185);
        btn_gobang.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) 
            {
                btn_gobang.setIcon(new ImageIcon(icon_gobang_float.getScaledInstance(260, 185, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseExited(MouseEvent e) 
            {
                btn_gobang.setIcon(new ImageIcon(icon_gobang.getScaledInstance(260, 185, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseClicked(MouseEvent e) 
            {
                /*執行五子棋遊戲*/
                Transfer transfer = new Transfer("Gobang",username);
            }
        });
        add(btn_gobang);
        /*-----------------------------------------------------*/
        Border bottom = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black);
        Border empty = new EmptyBorder(5, 0, 0, 0);
        Border border = new CompoundBorder(bottom, empty);
        /*-----------------------------------------------------*/
        text_gobang.setText("五子棋");
        text_gobang.setFont(new Font("標楷體", Font.PLAIN, 14));
        text_gobang.setHorizontalAlignment(JLabel.CENTER);
        text_gobang.setBorder(border);
        text_gobang.setBounds(60, 335, 220, 35);
        add(text_gobang);
    }
    private void close()
    {
        setState(Frame.ICONIFIED);
    }
    public void showPlatform()
    {
        setVisible(true);
    }
    private void setTray() 
    {
        if (SystemTray.isSupported()) // system tray supported
        {
            tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(imagepath+"support.png");
            /*----------------------------------------------------------------------*/
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
                {
                    System.exit(0);
                }
            });
            popup.add(defaultItem);
            defaultItem = new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) 
                {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            /*----------------------------------------------------------------------*/
            trayIcon = new TrayIcon(image, "Board-Game", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e) 
                {
                    if (e.getClickCount() >= 2) 
                    {
                        setVisible(true);
                        setExtendedState(JFrame.NORMAL);
                    }
                }
            });
        } 
        else 
        {
            System.out.println("system tray not supported");
        }
        /*----------------------------------------------------------------------*/
        addWindowStateListener(new WindowStateListener() 
        {
            public void windowStateChanged(WindowEvent e) 
            {
                if (e.getNewState() == ICONIFIED) 
                {
                    try 
                    {
                        tray.add(trayIcon);
                        setVisible(false);
                    } 
                    catch (AWTException ex) 
                    {
                        System.out.println("unable to add to tray");
                    }
                }
                if (e.getNewState() == 7) 
                {
                    try 
                    {
                        tray.add(trayIcon);
                        setVisible(false);
                    } 
                    catch (AWTException ex) 
                    {
                        System.out.println("unable to add to system tray");
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) 
                {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if (e.getNewState() == NORMAL) 
                {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            }
        });
    }
    
    public void initialization() 
    {
        ip = config.getConfig("IP");
    }
    
    public boolean EstablishConnection() 
    {
        try {
            sock = new Socket(ip, 8888);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintStream(sock.getOutputStream());
            connection_status = true;
            online();
            return true;
        } 
        catch (IOException ex) 
        {
            connection_status = false;
            return false;
        }
    }

    public void online() 
    {
        writer.println("User_online " + username);
        writer.flush();
    }
}