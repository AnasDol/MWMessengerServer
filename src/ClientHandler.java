import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private Server server;
    private Socket clientSocket = null;

    private static final String HOST = "localhost";
    private static final int PORT = 3443;

    private Scanner inMessage;
    private PrintWriter outMessage;
    private String clientMessage;

    private User user;


    public ClientHandler(Server server, Socket clientSocket) {
        try {
            this.server = server;
            this.clientSocket = clientSocket;
            this.inMessage = new Scanner(clientSocket.getInputStream());
            this.outMessage = new PrintWriter(clientSocket.getOutputStream());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            sendMessage("Подключение установлено");

            do {

                if (inMessage.hasNext()) {

                    clientMessage = inMessage.nextLine();
                    System.out.println(clientMessage);

                    // пользователь на экране регистрации нажал кнопку зарегистрироваться
                    if (clientMessage.equalsIgnoreCase("#register")) {
                        // принимаем имя и пароль
                        String name = waitForMessage();
                        String password = waitForMessage();
                        // проверяем имя на уникальность
                        if (Server.findUserByName(name)==null) {
                            user = new User(name, password);
                            user.clientHandler = this;
                            Server.addNewRegistredUser(user);
                            sendMessage("#register#ok");
                        }
                        else {
                            sendMessage("#register#failed");
                        }
                    }

                    // пользователь на экране регистрации нажал кнопку войти
                    else if (clientMessage.equalsIgnoreCase("#login")) {
                        // принимаем имя и пароль
                        String name = waitForMessage();
                        String password = waitForMessage();
                        User user = Server.findUserByName(name);
                        if (user!=null && password.equalsIgnoreCase(user.password)) {
                            this.user = user;
                            user.clientHandler = this;
                            sendMessage("#login#ok");
                        }
                        else {
                            sendMessage("#login#failed");
                        }
                    }

                    else if (clientMessage.equalsIgnoreCase("#join#chat")) {
                        // принимаем имя и пароль
                        String name = waitForMessage();
                        String password = waitForMessage();
                        Chat chat = Server.findChatByName(name);
                        if (chat!=null && password.equalsIgnoreCase(chat.password)) {
                            if (user.findChatByName(name)==null) {
                                user.chats.add(chat);
                                chat.users.add(user);
                            }
                            sendMessage("#join#chat#ok");
                        }
                        else {
                            sendMessage("#join#chat#failed");
                        }

                    }

                    else if (clientMessage.equalsIgnoreCase("#user#chats#request")) {
                        sendMessage("#user#chats");
                        for (Chat chat:user.chats) {
                            sendMessage(chat.toString());
                        }
                        sendMessage("#user#chats#end");
                    }

                    else if (clientMessage.equalsIgnoreCase("#global#chats#request")) {
                        sendMessage("#global#chats");
                        for (Chat chat:Server.getExistingChats()) {
                            if (user.findChatByName(chat.name)==null)
                                sendMessage(chat.toString());
                        }
                        sendMessage("#global#chats#end");
                    }

                    else if (clientMessage.equalsIgnoreCase("#messages#request")) {
                        String name = waitForMessage();
                        Chat chat = Server.findChatByName(name);
                        if (chat!=null) {
                            sendMessage("#messages");
                            for (Chat.Message message:chat.messages) {
                                sendMessage(message.toString());
                            }
                            sendMessage("#messages#end");
                        }
                    }

                    else if (clientMessage.equalsIgnoreCase("#message")) {
                        // принимаем имя и сообщение
                        String name = waitForMessage();
                        String message = waitForMessage();
                        Chat chat = Server.findChatByName(name);
                        if (chat!=null) {
                            chat.messages.add(new Chat.Message(user, message));
                            Server.sendMessagesToAllUsers(chat, "#update", chat.name);
                        }
                    }

                    else if (clientMessage.equalsIgnoreCase("#new#chat")) {
                        String name = waitForMessage();
                        String password = waitForMessage();
                        if (Server.findChatByName(name)==null) {
                            Chat chat = new Chat(name, password);
                            user.chats.add(chat);
                            chat.users.add(user);
                            Server.addNewExistingChat(chat);
                            sendMessage("#new#chat#ok");
                        }
                        else {
                            sendMessage("#new#chat#failed");
                        }
                    }

                }

                Thread.sleep(100);
            } while (!clientMessage.equalsIgnoreCase("#close#connection"));
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }

        finally {
            this.close();
        }

    }

    public void sendMessage(String message) {
        try {
            outMessage.println(message);
            outMessage.flush();
            System.out.println("sending: " + message);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public String waitForMessage() {
        String clientMsg = "";
        while(true){
            if (inMessage.hasNext()) {
                clientMsg = inMessage.nextLine();
                break;
            }
        }
        System.out.println("receiving: " + clientMsg);
        return clientMsg;
    }

    public void close() {
        user.clientHandler = null;
        inMessage.close();
        outMessage.close();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server.removeCurrentClient(this);

    }

}
