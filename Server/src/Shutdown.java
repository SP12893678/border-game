
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;

public class Shutdown extends RightSide {

    private static final long serialVersionUID = 1L;
    JLabel btn_close = new JLabel();

    public Shutdown()
    {
        setLayout(null);
        Component();
        setBackground();
    }
    public void Component()
    {
        /*-----------------------------------------------------*/
        btn_close.setText("Server Closed");
        btn_close.setFont(new Font("Fira Code Light", Font.PLAIN, 18));
        btn_close.setHorizontalAlignment(JLabel.CENTER);
        btn_close.setOpaque(true);
        btn_close.setForeground(Color.white);
        btn_close.setBackground(new Color(0, 0, 200, 51));
        btn_close.setBounds(200, 295, 200, 50);
        add(btn_close);
        /*-----------------------------------------------------*/

        /*-----------------------------------------------------*/

        /*-----------------------------------------------------*/
    }
}