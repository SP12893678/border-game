package listener;
import java.awt.Color;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldHintListener implements FocusListener 
{
    private String mHintText;
    private JTextField mtextField;
    private Boolean is_password = false; 

    public JTextFieldHintListener(String hintText, JTextField textField) 
    {
        this.mHintText = hintText;
        this.mtextField = textField;
        mtextField.setText(mHintText);
        textField.setForeground(Color.GRAY);
    }
    public JTextFieldHintListener(String hintText, JPasswordField textField) 
    {
        this.mHintText = hintText;
        this.mtextField = textField;
        mtextField.setText(mHintText);
        textField.setForeground(Color.GRAY);
        is_password = true;
    }
    @Override
    public void focusGained(FocusEvent e) 
    {
        String temp = mtextField.getText();
        if(temp.equals(mHintText))
        {
            mtextField.setText("");
            mtextField.setForeground(Color.BLACK);
            if(is_password)
               ((JPasswordField) mtextField).setEchoChar('•');
        }
    }
    @Override
    public void focusLost(FocusEvent e)
    {
        String temp = mtextField.getText();
        if(temp.equals(""))
        {
            mtextField.setForeground(Color.GRAY);
            mtextField.setText(mHintText);
            if (is_password)
                ((JPasswordField) mtextField).setEchoChar((char) 0);
        }
    }
}