package config;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class Config {
    private Properties props;
    private String fileop;

    public Config(String fileop) 
    {
        this.fileop = fileop;
        loadProperties();
    }

    public void loadProperties() 
    {
        props = new Properties();
        try 
        {
            props.load(new FileInputStream(fileop));
        } 
        catch (FileNotFoundException e) 
        {
            JOptionPane.showMessageDialog(null, "File not found", "Wrong", JOptionPane.WARNING_MESSAGE);
        } 
        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "IOException", "Wrong", JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getConfig(String key) 
    {
        return props.getProperty(key);
    }

    public void setConfg(String key, String value) 
    {
        props.setProperty(key, value);
        try 
        {
            props.store(new FileOutputStream(fileop), null);
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
