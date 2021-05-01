import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class Fontloader {
    public Fontloader() {
        String font = "./res/font/";
        try 
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(font + "LeelUIsl.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(font + "FiraCode-Light.ttf")));
        } 
        catch (IOException | FontFormatException e) 
        {
            // Handle exception
        }
    }
    
}