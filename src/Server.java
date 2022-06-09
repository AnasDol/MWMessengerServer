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

        Chat testChat1 = new Chat("СЛАВА НГТУ", "$null");
        Chat testChat2 = new Chat("авт-042 лучшие коты", "1234");
        Chat testChat3 = new Chat("шашлыки в субботу", "1234");
        Chat testChat4 = new Chat("в джазе только девушки", "$null");


        existingChats.add(testChat1);
        existingChats.add(testChat2);
        existingChats.add(testChat3);
        existingChats.add(testChat4);


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
