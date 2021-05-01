
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class Index extends RightSide {

    private static final long serialVersionUID = 1L;
    JLabel bar = new JLabel();

    public Index()
    {
        setLayout(null);
        Component();
        setBackground();
    }
    public void Component()
    {
        ip = new JLabel();
        login_online_count = new JLabel();
        user_online_count = new JLabel();
        gobang_online_count = new JLabel();
        server_start_time = new JLabel();
        /*-----------------------------------------------------*/
        Border bottom = BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black);
        Border empty = new EmptyBorder(0, 0, 0, 0);
        Border border = new CompoundBorder(bottom, empty);
        /*-----------------------------------------------------*/
        bar.setBounds(50, 100+25, 10, 150);
        bar.setBorder(border);
        add(bar);
        /*-----------------------------------------------------*/
        ip.setFont(new Font("Fira Code Light", Font.PLAIN, 16));
        ip.setBounds(75, 0+100+25, 200, 30);
        add(ip);
        /*-----------------------------------------------------*/
        server_start_time.setFont(new Font("Fira Code Light", Font.PLAIN, 16));
        server_start_time.setBounds(75, 30+100+25, 300, 30);
        add(server_start_time);
        /*-----------------------------------------------------*/
        login_online_count.setText("Login Page → 0");
        login_online_count.setFont(new Font("Fira Code Light", Font.PLAIN, 16));
        login_online_count.setBounds(75, 60+100+25, 200, 30);
        add(login_online_count);
        /*-----------------------------------------------------*/
        user_online_count.setText("User Online → 0");
        user_online_count.setFont(new Font("Fira Code Light", Font.PLAIN, 16));
        user_online_count.setBounds(75, 90+100+25, 200, 30);
        add(user_online_count);
        /*-----------------------------------------------------*/
        gobang_online_count.setText("Gobang Online → 0");
        gobang_online_count.setFont(new Font("Fira Code Light", Font.PLAIN, 16));
        gobang_online_count.setBounds(75, 120+100+25, 200, 30);
        add(gobang_online_count);
        /*-----------------------------------------------------*/

        /*-----------------------------------------------------*/
    }
}