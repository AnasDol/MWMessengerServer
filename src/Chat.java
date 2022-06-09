import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat {

    public String name;
    public String password; // чат считается публичным, если пароль - пустая строка
    public String lastMessage;

    public ArrayList<Message> messages = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();

    public Chat (String name, String password) {
        this.name = name;
        this.password = password;
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

        public String toString() { // #маша#red#12 дек. 2022#привет

            String color = sender.color.getRed()+"/"+sender.color.getGreen()+"/"+sender.color.getBlue();

            return "#"+sender.name+"#"+color+"#"+(new SimpleDateFormat("d MMM y H:mm").format(dateTime))+"#"+text;

        }

    }

}
