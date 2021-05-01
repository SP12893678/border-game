import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

import transfer.Transfer;
public class Client {
    public static void main(String[] args) {
        //SetLookAndFeel
        try 
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        String fontPath = "./res/font/";
        try 
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "LEVIBRUSH.TTF")));
        } 
        catch (IOException | FontFormatException e) 
        {
            // Handle exception
        }
        Transfer transfer = new Transfer("Login");
    }
    
}