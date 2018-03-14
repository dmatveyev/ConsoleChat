package server;

import server.clientData.Session;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;
import server.messagePool.Message;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Created by Денис on 06.03.2018.
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private PrintWriter out;
    private User user;
    private UsersManager usersManager;
    private UserSessionManager userSessionManager;

    public ClientHandler(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.usersManager = UsersManager.getInstance();
        this.userSessionManager = UserSessionManager.getInstance();
        try {
            this.out  = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream(),
                    "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try(InputStream inputStream = clientSocket.getInputStream()) {
            Scanner in = new Scanner(inputStream, "UTF-8");
            user = null;
            while (user == null) {
                String login = "";
                String pass= "";
                out.println("Hello, please enter login:");
                if (in.hasNextLine()) {
                    login = in.nextLine();
                }
                out.println("Please enter password");
                if (in.hasNextLine()) {
                    pass = in.nextLine();
                }
                user = usersManager.authorize(login, pass);
            }
            out.printf("Hello, %s!!!", user.getLogin());
            out.println();
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("exit")) {
                    Session ss =  userSessionManager.isActive(user);
                    ss.setName(null);
                    userSessionManager.doUnactive(ss);
                    clientSocket.close();
                    out.close();
                } else {
                    Message message = new Message(line, user, LocalDate.now(), LocalTime.now());
                    server.sendMessageToAll(message.toString());
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.err.printf ("Server error message: %s", e.getMessage());

        } finally {
            if(user != null) {
                Session ss = userSessionManager.isActive(user);
                ss.setName(null);
                userSessionManager.doUnactive(ss);
            }
        }
    }

    public void printMessage(String message) {
        out.println(message);
    }
}
