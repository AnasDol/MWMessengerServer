import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat {

    public String name;
    public String password; // чат считается публичным, если пароль - пустая строка
    public String history;
    public String lastMessage;

    public ArrayList<Message> messages = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();

    public Chat (String name, String password) {
        this.name = name;
        this.password = password;
        this.history = "";
    }

    public String toString() {
        if (password.equalsIgnoreCase("$null"))
            return "#"+name+"#public#"+lastMessage;
        else return "#"+name+"#private#"+lastMessage;
    }

    public static class Message {

        User sender;

        Date dateTime;

        String text;



        public Message(User sender, String text) {

            this.sender = sender;
            this.dateTime = new Date();
            this.text = text;

        }

        public String toString() {

            return "#"+sender.name+"#"+(new SimpleDateFormat("d MMM y H:m").format(dateTime))+"#"+text;

        }

    }

}
