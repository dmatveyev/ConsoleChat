package server;



import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;
import server.messagePool.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private int clientId;
    private PrintWriter out;
    private User user;
    private UsersManager usersManager;
    private UserSessionManager userSessionManager;

    public ClientHandler(Server server, Socket clientSoket, int clientId) {
        this.clientId = clientId;
        this.clientSocket = clientSoket;
        this.server = server;
        this.usersManager = UsersManager.getInstance();
        this.userSessionManager = UserSessionManager.getInstance();
        try {
            this.out  = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try(InputStream inputStream = clientSocket.getInputStream()) {
            Scanner in = new Scanner(inputStream, "UTF-8");
            String login = "";
            String pass= "";
            user = null;
            while (user == null) {
                out.println("Hello, please enter login:");
                if (in.hasNextLine()) {
                    login = in.nextLine();
                }
                out.println("Please enter password");
                if (in.hasNextLine()) {
                    pass = in.nextLine();
                }
                user = authorize(login, pass);
            }
            out.printf("Hello, %s!!!", user.getLogin());
            out.println();
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("exit")) {
                    userSessionManager.doUnactive(user.getUserId());
                    clientSocket.close();
                    out.close();
                } else {
                    Message message = new Message(line, user, LocalDate.now(), LocalTime.now());
                    server.sendMessageToAll(message.toString());
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(user != null)
                userSessionManager.doUnactive(user.getUserId());
        }
    }

    public void printMessage(String message) {
        out.println(message);
    }

    /**
     * Проверяет введенные данные и авторизует пользователя.
     * @param login
     * @param password
     * @return Зарегистрированный или новый пользователь
     * @throws IOException
     */
    public User authorize(String login, String password) throws IOException {
        User user;
        String userId = usersManager.isRegistered(login, password);
        if (userId!= null) {
            user = usersManager.getRegisteredUser(userId);
            if (userSessionManager.isActive(user) == null) {
                userSessionManager.doActive(userId,
                        userSessionManager.createUserSession(user));
                return user;
            } else {
                throw new IOException("User allredy authorized");
            }
        }else {
            user = new User ();
            user.setLogin(login);
            user.setPassword(password);
            user.setUserId(String.valueOf(Math.random()));
            userSessionManager.doActive(user.getUserId(),
                    userSessionManager.createUserSession(user));
            return user;
        }
    }
}
