import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static final int PORT = 3443;

    private static ArrayList<User> registredUsers = new ArrayList<User>(); // должно сохраняться
    private static ArrayList<Chat> existingChats = new ArrayList<Chat>(); // должно сохраняться
    private static ArrayList<ClientHandler> currentClients = new ArrayList<>();


    public Server() {

        Chat testChat1 = new Chat("Туса на алтае в субботу", "$null");
        testChat1.history = "Глеб: прив, кто поедет? Давид: я точно нет";
        testChat1.lastMessage = "Давид: я точно нет";

        Chat testChat2 = new Chat("BigBadGuys", "$null");
        testChat2.history = "Колян: ну вы да конечно";
        testChat2.lastMessage = "Колян: ну вы да конечно";

        Chat testChat3 = new Chat("Девачки2", "1234");
        testChat3.history = "Катя: придешь? Таня: конечно";
        testChat3.lastMessage = "Таня: конечно";


        existingChats.add(testChat1);
        existingChats.add(testChat2);
        existingChats.add(testChat3);


        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("сервер запущен");

            while (true) {

                clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(this, clientSocket);
                currentClients.add(client);
                new Thread(client).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("cервер остановлен");
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static User findUserByName(String name) {
        for (User user : registredUsers) {
            if (user.name.equalsIgnoreCase(name)) return user;
        }
        return null;
    }

    public static void addNewRegistredUser(User user) {
        registredUsers.add(user);
    }

    public static void removeCurrentClient(ClientHandler client) {
        currentClients.remove(client);
    }

    public static ArrayList<Chat> getExistingChats() {
        return existingChats;
    }

    public static Chat findChatByName(String name) {
        for (Chat chat : existingChats) {
            if (chat.name.equalsIgnoreCase(name)) return chat;
        }
        return null;
    }

    public static void addNewExistingChat(Chat chat) {
        existingChats.add(chat);
    }

    public static void sendMessagesToAllUsers(Chat chat, String... strings) {
        for (User user:chat.users) {
            if (user.clientHandler!=null) {
                for (String s:strings) {
                    user.clientHandler.sendMessage(s);
                }
            }
        }
    }
}
