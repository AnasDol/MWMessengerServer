import java.util.ArrayList;

public class User {

    public long id;
    public String name;
    public String password;
    public ArrayList<Chat> chats;
    public ClientHandler clientHandler;

    public static long user_number = 0;

    public User (/*long id, */String name, String password) {
        //this.id = id;
        this.id = user_number++;
        this.name = name;
        this.password = password;
        chats = new ArrayList<>();
    }

    public Chat findChatByName(String name) {
        for (Chat chat : chats) {
            if (chat.name.equalsIgnoreCase(name)) return chat;
        }
        return null;
    }



}
