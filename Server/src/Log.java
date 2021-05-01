
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;

public class Log extends RightSide {

    private static final long serialVersionUID = 1L;
    JLabel logbg = new JLabel();
    public JScrollPane listScroll = new JScrollPane();

    public Log()
    {
        setLayout(null);
        Component();
        setBackground();
    }
    public void Component()
    {
        /*-----------------------------------------------------*/
        logArea = new JTextArea();
        logArea.setMargin(new Insets(10,20,10,20));
        logArea.setFont(new Font("Fira Code Light", Font.PLAIN, 16));
        logArea.setForeground(Color.white);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setOpaque(false);
        logArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollBar vertical = listScroll.getVerticalScrollBar();
        vertical.setPreferredSize(new Dimension(0, 0));
        /*-----------------------------------------------------*/
        listScroll.setViewportView(logArea);
        listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScroll.getViewport().setOpaque(false);
        listScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        listScroll.getVerticalScrollBar().setUnitIncrement(10);
        listScroll.setOpaque(false);
        listScroll.setVerticalScrollBar(vertical);
        listScroll.setBorder(null);
        listScroll.setBounds(20, 150, 720, 470);
        add(listScroll);
        /*-----------------------------------------------------*/
        logbg.setOpaque(true);
        logbg.setBackground(new Color(0, 0, 0, 51));
        logbg.setBounds(20, 150, 720, 470);
        add(logbg);
        /*-----------------------------------------------------*/

        /*-----------------------------------------------------*/
    }
}