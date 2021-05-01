package login;

import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import config.Config;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import listener.JTextFieldHintListener;
import listener.DragListener;
import transfer.Transfer;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    Config config = new Config("./res/config/config.properties");
    String name, ip,username,password;
    Socket sock;
    Thread readerThread;
    BufferedReader reader;
    PrintStream writer;
    boolean connection_status = false;
    boolean login_status = false;
    int sign_up_count = 0;

    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath+"gold.png").getImage();
    private Image icon_cursor = new ImageIcon(imagepath+"cursor.png").getImage();
    private Image icon_close = new ImageIcon(imagepath+"close.png").getImage();
    private Image icon_close_float = new ImageIcon(imagepath+"close-float.png").getImage();
    private JLabel btn_close = new JLabel();
    private Image icon_title = new ImageIcon(imagepath + "hourse.png").getImage();
    private JLabel logo_title = new JLabel();
    private Image icon_username = new ImageIcon(imagepath+"user.png").getImage();
    private Image icon_password = new ImageIcon(imagepath+"lock.png").getImage();
    private JLabel logo_username = new JLabel();
    private JLabel logo_password = new JLabel();
    private JTextField input_username = new JTextField();
    private JPasswordField input_password = new JPasswordField();
    private Image icon_view_password = new ImageIcon(imagepath+"password-view.png").getImage();
    private Image icon_view_password_float = new ImageIcon(imagepath+"password-view-float.png").getImage();
    private JLabel btn_view_password = new JLabel();
    // private Image icon_checkbox = new ImageIcon(imagepath + "checkbox.png").getImage();
    // private Image icon_checkbox_select = new ImageIcon(imagepath + "checkbox-select.png").getImage();
    private JCheckBox remember_password = new JCheckBox();
    private Image icon_error = new ImageIcon(imagepath + "error.png").getImage();
    private JLabel error_message = new JLabel();
    private JLabel btn_login = new JLabel();
    private JLabel btn_sign_up = new JLabel();
    public Login()
    {
        setVisible(false);
        setFocusable(true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage(imagepath+"login.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(400, 700);
        setLocationRelativeTo(null);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(icon_cursor, new Point(9, 0), "img");
        setCursor(cursor);
        DragListener drag = new DragListener();
        addMouseListener(drag);
        addMouseMotionListener(drag);
    }
    public void Component()
    {
        setContentPane(new JLabel(new ImageIcon(background)));
        /*-----------------------------------------------------*/
        btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        btn_close.setBounds(378, 5, 16, 16);
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
        logo_title.setIcon(new ImageIcon(icon_title.getScaledInstance(250, 250, BufferedImage.SCALE_SMOOTH)));
        logo_title.setBounds(70, 55, 250, 250);
        add(logo_title);
        /*-----------------------------------------------------*/
        logo_username.setIcon(new ImageIcon(icon_username.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        logo_username.setBounds(100, 295, 32, 32);
        add(logo_username);
        /*-----------------------------------------------------*/
        logo_password.setIcon(new ImageIcon(icon_password.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        logo_password.setBounds(100, 345, 32, 32);
        add(logo_password);
        /*-----------------------------------------------------*/
        btn_view_password.setIcon(new ImageIcon(icon_view_password.getScaledInstance(24, 12, BufferedImage.SCALE_SMOOTH)));
        btn_view_password.setBounds(285, 355, 24, 12);
        btn_view_password.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e) 
            {
                btn_view_password.setIcon(
                        new ImageIcon(icon_view_password_float.getScaledInstance(24, 12, BufferedImage.SCALE_SMOOTH)));
            }
            public void mouseExited(MouseEvent e) 
            {
                btn_view_password.setIcon(
                        new ImageIcon(icon_view_password.getScaledInstance(24, 12, BufferedImage.SCALE_SMOOTH)));
            }
            public void mousePressed(MouseEvent e) 
            {
                input_password.setEchoChar((char) 0);
            }
            public void mouseReleased(MouseEvent e) 
            {
                if(!String.valueOf(input_password.getPassword()).equals("Password"))
                    input_password.setEchoChar('•');
            }
        });
        add(btn_view_password);
        /*-----------------------------------------------------*/
        Border bottom = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white);
        Border empty = new EmptyBorder(0, 30, 0, 30);
        Border border = new CompoundBorder(bottom, empty);
        /*-----------------------------------------------------*/
        input_username.addFocusListener(new JTextFieldHintListener("Username", input_username));
        input_username.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 16));
        input_username.setOpaque(false);
        input_username.setBorder(border);
        input_username.setBounds(100, 300, 210, 25);
        add(input_username);
        /*-----------------------------------------------------*/
        input_password.addFocusListener(new JTextFieldHintListener("Password", input_password));
        input_password.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 16));
        input_password.setOpaque(false);
        input_password.setBorder(border);
        input_password.setBounds(100, 350, 210, 25);
        input_password.setEchoChar((char) 0);
        add(input_password);
        /*-----------------------------------------------------*/
        Color remember_passwordColor = new Color(93, 93, 93);
        /*-----------------------------------------------------*/
        remember_password.setText("記住資訊");
        remember_password.setForeground(remember_passwordColor);
        remember_password.setFont(new Font("新細明體", Font.PLAIN, 12));
        remember_password.setOpaque(false);
        remember_password.setFocusPainted(false);
        //remember_password.setIconTextGap(10);
        //remember_password.setIcon(new ImageIcon(icon_checkbox.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        //remember_password.setSelectedIcon(new ImageIcon(icon_checkbox_select.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        remember_password.setBounds(97, 382, 100, 30);
        add(remember_password);
        /*-----------------------------------------------------*/
        error_message.setIcon(new ImageIcon(icon_error.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        error_message.setVisible(false);
        error_message.setForeground(Color.red);
        error_message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        error_message.setBounds(100, 410, 280, 30);
        add(error_message);
        /*-----------------------------------------------------*/
        Color login_Color = new Color(255, 195, 51);
        Color login_enterColor = new Color(233, 178, 47);
        /*-----------------------------------------------------*/
        btn_login.setText("Login");
        btn_login.setForeground(Color.white);
        btn_login.setFont(new Font("Microsoft Tai Le", Font.BOLD, 16));
        btn_login.setOpaque(true);
        btn_login.setBackground(login_Color);
        btn_login.setHorizontalAlignment(JLabel.CENTER);
        btn_login.setBounds(100, 455, 210, 40);
        btn_login.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) 
            {
                btn_login.setBackground(login_enterColor);
            }
            public void mouseExited(MouseEvent e) 
            {
                btn_login.setBackground(login_Color);
            }
            public void mouseClicked(MouseEvent e)
            {
                if (check() && is_connect()) 
                {
                    sendLogin();
                }
            }
        });
        add(btn_login);
        /*-----------------------------------------------------*/
        Color sign_up_Color = new Color(255, 255, 255, 100);
        /*-----------------------------------------------------*/
        btn_sign_up.setText("Sign Up");
        btn_sign_up.setForeground(Color.white);
        btn_sign_up.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn_sign_up.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, sign_up_Color));
        btn_sign_up.setHorizontalAlignment(JLabel.CENTER);
        btn_sign_up.setBounds(100, 505, 210, 30);
        btn_sign_up.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn_sign_up.setForeground(login_Color);
                btn_sign_up.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, login_Color));
            }

            public void mouseExited(MouseEvent e) {
                btn_sign_up.setForeground(Color.white);
                btn_sign_up.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, sign_up_Color));
            }
            public void mouseClicked(MouseEvent e) 
            {
                if (check() && is_connect()) 
                {
                    sendSignup();
                }
            }
        });
        add(btn_sign_up);
    }
    public void initialization() 
    {
        if(config.getConfig("Remember").equals("true"))
        {
            remember_password.setSelected(true);
            if ((ip = config.getConfig("IP")) == null)
                error_message.setText("設定檔未設定IP");
            if ((username = config.getConfig("Username")) != null) {
                input_username.setText(username);
                input_username.setForeground(Color.black);
            }
            if ((password = config.getConfig("Password")) != null) {
                input_password.setText(password);
                input_password.setEchoChar('•');
                input_password.setForeground(Color.black);
            }
        }
        else
        {
            remember_password.setSelected(false);
        }
    }       

    private void close()
    {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    public void showLogin()
    {
        setVisible(true);
    }
    public void hiddenLogin() 
    {
        setVisible(false);
    }

    public void online() 
    {
        writer.println("Login_online " + "online");
        writer.flush();
    }
    
    public void runThread()
    {
        readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }
    public boolean EstablishConnection() 
    {
        try 
        {
            sock = new Socket(ip, 8888);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintStream(sock.getOutputStream());
            connection_status = true;
            runThread();
            online();
            return true;
        } 
        catch (IOException ex) 
        {
            error_message.setText("網路連線失敗");
            error_message.setVisible(true);
            connection_status = false;
            return false;
        }
    }

    public boolean is_connect() 
    {
        if (connection_status)
            return true;
        else if (EstablishConnection())
            return true;
        else
            return false;
    }

    public class IncomingReader implements Runnable 
    {
        public void run() 
        {
            String message;
            String action;
            String result;
            try 
            {
                while ((message = reader.readLine()) != null) 
                {
                    action = message.substring(0, message.indexOf(" "));
                    result = message.substring(message.indexOf(" ") + 1);
                    switch (action) 
                    {
                    case "Login":
                        returnLogin(result);
                        break;
                    case "Sign_up":
                        returnSignup(result);
                        break;
                    default:
                        error_message.setText("default action");
                        break;
                    }
                    if(login_status)
                        break;
                }
                transferTo();
            } 
            catch (Exception ex) 
            {
                ex.printStackTrace();
            }
        }
    }

    private boolean check() 
    {
        if(input_username.getText().equals("") || input_username.getText().equals("Username"))
        {
            error_message.setText("用戶欄為空白");
            error_message.setVisible(true);
            return false;
        }     
        else if(String.valueOf(input_password.getPassword()).equals("") || String.valueOf(input_password.getPassword()).equals("Username"))
        {
            error_message.setText("密碼為空白");
            error_message.setVisible(true);
            return false;
        }
        else
        {
            error_message.setVisible(false);
            return true;
        }
    }

    private void sendLogin() 
    {
        try 
        {
            writer.println("Login " + input_username.getText() + "|" + String.valueOf(input_password.getPassword()));
            writer.flush();
        } 
        catch (Exception ex) 
        {
            System.out.println("送出資料失敗");
        }
    }

    private void returnLogin(String result) 
    {
        if (result.equals("success"))
        {
            error_message.setText("登入成功");
            error_message.setVisible(true);
            login_status = true;
        }
        else if(result.equals("username not found"))
        {
            error_message.setText("用戶不存在");
            error_message.setVisible(true);
        }
        else if(result.equals("login already exists"))
        {
            error_message.setText("用戶已登入了");
            error_message.setVisible(true);
        }
        else if (result.equals("wrong password"))
        {
            error_message.setText("密碼錯誤");
            error_message.setVisible(true);
        }
    }

    private void sendSignup() 
    {
        if(sign_up_count < 5)
        {
            sign_up_count++;
            try 
            {
                writer.println("Sign_up " + input_username.getText() + "|" + String.valueOf(input_password.getPassword()));
                writer.flush();
            } 
            catch (Exception ex) 
            {
                System.out.println("送出資料失敗");
            }
        } 
        else 
        {
            error_message.setText("註冊次數過多!");
            error_message.setVisible(true);
        }
    }

    private void returnSignup(String result) 
    {
        if (result.equals("username not found"))
        {
            error_message.setText("註冊成功");
            error_message.setVisible(true);
        }   
        else if (result.equals("username is exists")) 
        {
            error_message.setText("用戶已存在");
            error_message.setVisible(true);
        }
    }

    public void transferTo()
    {
        try 
        {
            writer.close();
            reader.close();
            sock.close();
        } 
        catch (Exception e) 
        {
            System.out.println("關閉失敗");
        }
        if(remember_password.isSelected())
        {
            config.setConfg("Username", input_username.getText());
            config.setConfg("Password", String.valueOf(input_password.getPassword()));
            config.setConfg("Remember", "true");
        }
        else
        {
            config.setConfg("Remember", "false");
        }
        Transfer transfer = new Transfer("Platform", input_username.getText());
        dispose();
    }
}