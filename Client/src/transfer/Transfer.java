package transfer;

import login.Login;
import platform.Platform;
import game.gobang.Gobang;

public class Transfer {

    
    String username;
    public static Boolean game_Status = false;

    Login login;
    Platform platform;
    public static Gobang gobang;

    public Transfer(String type)
    {
        transfer(type);
    }
    public Transfer(String type,String username) 
    {
        this.username = username;
        transfer(type);
    }
    private void transfer(String type) 
    {
        switch (type) 
        {
            case "Login":
                login = new Login();
                login.Component();
                login.initialization();
                login.EstablishConnection();
                login.showLogin();
                break;
            case "Platform":
                platform = new Platform(username);
                platform.Component();
                platform.initialization();
                platform.EstablishConnection();
                platform.showPlatform();
                break;
            case "Gobang":
                if(!game_Status)
                {
                    game_Status = true;
                    gobang = new Gobang(username);
                    gobang.Component();
                    gobang.initialization();
                    gobang.EstablishConnection();
                    gobang.showGobang();
                }
                break;
            default:
                break;
        }
    }
}