package game.gobang;

import java.io.*;
import java.net.*;

import javax.swing.*;

import config.Config;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

import listener.DragListener;
import sound.Sound;
import transfer.Transfer;
import game.gobang.onlinemode.CreatRoom;
import game.gobang.onlinemode.JoinDialog;
import game.gobang.onlinemode.WaitRoom;

public class Gobang extends JFrame {

    private static final long serialVersionUID = 1L;
    Config config = new Config("./res/config/config.properties");
    String username;
    String name, ip = "";
    BufferedReader reader;
    PrintStream writer;
    Socket sock;
    boolean connection_status = false;
    public static boolean disable = false;
    String mode = "index";

    CreatRoom creatRoom = new CreatRoom(this);
    WaitRoom waitRoom = new WaitRoom(this);
    JoinDialog joinDialog = new JoinDialog(this);

    GobangGUI gobangGUI = new GobangGUI();
    String musicPath = "./res/music/";
    Sound bgmusic = new Sound(musicPath + "gobang_bg.mp3",true,0.3);
    Sound press = new Sound(musicPath + "chess.mp3", false, 1);
    private String imagepath = "./res/images/";
    private Image background = new ImageIcon(imagepath + "gobang-bg2.png").getImage();
    private Image icon_close = new ImageIcon(imagepath + "close.png").getImage();
    private Image icon_close_float = new ImageIcon(imagepath + "close-float.png").getImage();
    private JLabel btn_close = new JLabel();
    private Image icon_back = new ImageIcon(imagepath + "back.png").getImage();
    private Image icon_back_float = new ImageIcon(imagepath + "back-float.png").getImage();
    private JLabel btn_back = new JLabel();

    private JLabel btn_chess_one = new JLabel();
    private JLabel btn_chess_two = new JLabel();
    private JLabel btn_chess_three = new JLabel();
    private Image icon_single = new ImageIcon(imagepath + "single.png").getImage();
    private Image icon_double = new ImageIcon(imagepath + "double.png").getImage();
    private Image icon_online = new ImageIcon(imagepath + "online.png").getImage();
    private Image icon_creat_room = new ImageIcon(imagepath + "creat-room.png").getImage();
    private Image icon_join_room = new ImageIcon(imagepath + "join-room.png").getImage();
    private Image icon_random_match = new ImageIcon(imagepath + "random-match.png").getImage();

    private JLabel option_float = new JLabel();
    private Image icon_option_black_float = new ImageIcon(imagepath + "option-black-float.png").getImage();
    private Image icon_option_white_float = new ImageIcon(imagepath + "option-white-float.png").getImage();

    public Gobang(String username) {
        this.username = username;
        setVisible(false);
        setFocusable(true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage(imagepath + "icon-gobang.png"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(1260, 680);
        setLocationRelativeTo(null);
        DragListener drag = new DragListener();
        addMouseListener(drag);
        addMouseMotionListener(drag);
        bgmusic.playSound();
    }

    public void Component() {
        indexmode();
        setContentPane(new JLabel(new ImageIcon(background.getScaledInstance(1260, 680, BufferedImage.SCALE_SMOOTH))));
        /*-----------------------------------------------------*/
        btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
        btn_close.setBounds(1238, 5, 16, 16);
        btn_close.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn_close
                        .setIcon(new ImageIcon(icon_close_float.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseExited(MouseEvent e) {
                btn_close.setIcon(new ImageIcon(icon_close.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseClicked(MouseEvent e) {
                if (!disable)
                    close();
            }
        });
        add(btn_close);
        /*-----------------------------------------------------*/
        option_float.setVisible(false);
        add(option_float);
        /*-----------------------------------------------------*/
        btn_chess_one.setBounds(150, 380, 120, 30);
        btn_chess_one.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                option_float.setIcon(
                        new ImageIcon(icon_option_black_float.getScaledInstance(130, 166, BufferedImage.SCALE_SMOOTH)));
                option_float.setBounds(175, 275, 130, 166);
                option_float.setVisible(true);
            }

            public void mouseExited(MouseEvent e) {
                option_float.setVisible(false);
            }

            public void mouseClicked(MouseEvent e) {
                if (!disable) {
                    if (mode.equals("index"))
                        ;/* single-mode */
                    else if (mode.equals("online") && is_connect())
                        creatRoom.showDialog();
                    /* run creat room */
                }
            }
        });
        add(btn_chess_one);
        /*-----------------------------------------------------*/
        btn_chess_two.setBounds(555, 390, 120, 30);
        btn_chess_two.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                option_float.setIcon(
                        new ImageIcon(icon_option_white_float.getScaledInstance(130, 166, BufferedImage.SCALE_SMOOTH)));
                option_float.setBounds(585, 285, 130, 166);
                option_float.setVisible(true);
            }

            public void mouseExited(MouseEvent e) {
                option_float.setVisible(false);
            }

            public void mouseClicked(MouseEvent e) {
                if (!disable) {
                    if (mode.equals("index"))
                        new GobangGUI();
                    /* double-mode */
                    else if (mode.equals("online") && is_connect())
                        joinroom_List();
                    /* run join room */
                }
            }
        });
        add(btn_chess_two);
        /*-----------------------------------------------------*/
        btn_chess_three.setBounds(860, 525, 160, 40);
        btn_chess_three.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                option_float.setIcon(
                        new ImageIcon(icon_option_black_float.getScaledInstance(130, 166, BufferedImage.SCALE_SMOOTH)));
                option_float.setBounds(925, 430, 130, 166);
                option_float.setVisible(true);
            }

            public void mouseExited(MouseEvent e) {
                option_float.setVisible(false);
            }

            public void mouseClicked(MouseEvent e) {
                if (!disable) {
                    if (mode.equals("index") && is_connect())
                        onlinemode();
                    /* online-mode */
                    else if (mode.equals("online") && is_connect()) {
                        writer.println("Gobang_Random_Match " + username);
                        writer.flush();
                        /* run random match */
                    }
                }
            }
        });
        add(btn_chess_three);
        /*-----------------------------------------------------*/
        btn_back.setIcon(new ImageIcon(icon_back.getScaledInstance(115, 37, BufferedImage.SCALE_SMOOTH)));
        btn_back.setBounds(20, 5, 115, 37);
        btn_back.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn_back.setIcon(new ImageIcon(icon_back_float.getScaledInstance(115, 37, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseExited(MouseEvent e) {
                btn_back.setIcon(new ImageIcon(icon_back.getScaledInstance(115, 37, BufferedImage.SCALE_SMOOTH)));
            }

            public void mouseClicked(MouseEvent e) {
                if (!disable)
                    indexmode();
            }
        });
        add(btn_back);
        /*-----------------------------------------------------*/
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Transfer.game_Status = false;
                e.getWindow().dispose();
            }
        });
    }

    private void close() {
        disable = false;
        Transfer.game_Status = false;
        try {
            writer.close();
            reader.close();
            sock.close();
        } catch (Exception e) {
            System.out.println("關閉失敗");
        }
        bgmusic.stopSound();
        dispose();
    }

    public void showGobang() {
        setVisible(true);
    }

    public void hiddenGobang() {
        setVisible(false);
    }

    public void indexmode() {
        mode = "index";
        btn_back.setVisible(false);
        btn_chess_one.setIcon(new ImageIcon(icon_single.getScaledInstance(100, 25, BufferedImage.SCALE_SMOOTH)));
        btn_chess_two.setIcon(new ImageIcon(icon_double.getScaledInstance(100, 25, BufferedImage.SCALE_SMOOTH)));
        btn_chess_three.setIcon(new ImageIcon(icon_online.getScaledInstance(140, 35, BufferedImage.SCALE_SMOOTH)));
    }

    public void onlinemode() {
        mode = "online";
        btn_back.setVisible(true);
        btn_chess_one.setIcon(new ImageIcon(icon_creat_room.getScaledInstance(100, 25, BufferedImage.SCALE_SMOOTH)));
        btn_chess_two.setIcon(new ImageIcon(icon_join_room.getScaledInstance(100, 25, BufferedImage.SCALE_SMOOTH)));
        btn_chess_three
                .setIcon(new ImageIcon(icon_random_match.getScaledInstance(140, 35, BufferedImage.SCALE_SMOOTH)));
    }

    public void runThread() {
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    public boolean EstablishConnection() {
        try {
            sock = new Socket(ip, 8888);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintStream(sock.getOutputStream());
            creatRoom.setWriter(writer);
            waitRoom.setWriter(writer);
            joinDialog.setWriter(writer);

            gobangGUI.setWriter(writer);

            connection_status = true;
            runThread();
            online();
            return true;
        } catch (IOException ex) {
            connection_status = false;
            return false;
        }
    }

    public boolean is_connect() {
        if (connection_status)
            return true;
        else if (EstablishConnection())
            return true;
        else
            return false;
    }

    public void initialization() {
        ip = config.getConfig("IP");
    }

    public void online() {
        writer.println("Gobang_online " + username);
        writer.flush();
    }

    public void joinroom_List() {
        joinDialog = new JoinDialog(this);
        joinDialog.setWriter(writer);
        writer.println("Gobang_Room_List " + username);
        writer.flush();
    }

    public class IncomingReader implements Runnable {

        public void run() {
            Object obj;
            String message = "";
            int times = 1;
            String action;
            String result;
            String num;
            String title;
            String player1;
            String player2;
            try {
                while ((obj = reader.readLine()) != null) {
                    if (obj.getClass().getName() instanceof String) {
                        message = obj.toString();
                        System.out.println(message);
                        action = message.substring(0, message.indexOf(" "));
                        result = message.substring(message.indexOf(" ") + 1);
                        switch (action) {
                        case "Creat_Room":
                            num = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            title = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            player1 = result;
                            waitRoom.initialize(num, title, player1, "#玩家名稱");
                            waitRoom.setRoomMaster(true);
                            creatRoom.closeDialog();
                            waitRoom.showDialog();
                            break;
                        case "New_List":
                            times = 1;
                            break;
                        case "Join_List":
                            num = result.substring(0, result.indexOf("|"));
                            String room_title = result.substring(result.indexOf("|") + 1,
                                    result.indexOf("|", (result.indexOf("|") + 1)));
                            String room_master = result.substring(result.indexOf("|", (result.indexOf("|") + 1)) + 1);
                            joinDialog.roomlist.creatRoomList(times, num, room_title, room_master);
                            times++;
                            break;
                        case "Show_List":
                            joinDialog.showDialog();
                            break;
                        case "Join_Room":
                            num = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            title = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            player1 = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            player2 = result;
                            waitRoom.initialize(num, title, player1, player2);
                            waitRoom.setRoomMaster(false);
                            joinDialog.closeDialog();
                            waitRoom.showDialog();
                            break;
                        case "Refresh_Room":
                            num = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            title = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            player1 = result.substring(0, result.indexOf("|"));
                            result = result.substring(result.indexOf("|") + 1);
                            player2 = result;
                            waitRoom.initialize(num, title, player1, player2);
                            break;
                        case "Exit_Room":
                            waitRoom.refresh();
                            waitRoom.setRoomMaster(true);
                            break;
                        case "Error":
                            JOptionPane.showMessageDialog(null, result);
                            break;
                        case "test":
                            gobangGUI = new GobangGUI();
                            gobangGUI.setUser(username);
                            gobangGUI.setVisible(true);
                            waitRoom.setVisible(false);
                            break;
                        case "Piece":
                            press.newMedia();
                            press.playSound();
                            int x = Integer.valueOf(result.substring(0, result.indexOf("|")));
                            result = result.substring(result.indexOf("|") + 1);
                            int y = Integer.valueOf(result.substring(0, result.indexOf("|")));
                            int i = Integer.valueOf(result.substring(result.indexOf("|") + 1));
                            gobangGUI.getBoardPanel().addPiece(y, x, i);
                            break;
                        case "State":
                            JOptionPane.showMessageDialog(gobangGUI, "You " + result);
                            writer.println("Ready " + username);
                            gobangGUI.setVisible(false);
                            gobangGUI.dispose();
                            waitRoom.setVisible(true);
                            break;
                        default:
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}