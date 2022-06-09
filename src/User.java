import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class User {

    public long id;
    public String name;
    public String password;
    public ArrayList<Chat> chats;
    public ClientHandler clientHandler;
    public Color color;

    public static long user_number = 0;

    public User (/*long id, */String name, String password) {
        //this.id = id;
        this.id = user_number++;
        this.name = name;
        this.password = password;
        chats = new ArrayList<>();
        color = new Color(new Random().nextInt(0xFFFFFF));
    }

    public Chat findChatByName(String name) {
        for (Chat chat : chats) {
            if (chat.name.equalsIgnoreCase(name)) return chat;
        }
        return null;
    }



}
