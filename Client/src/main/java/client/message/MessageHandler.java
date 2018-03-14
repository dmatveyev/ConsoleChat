package client.message;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Created by Денис on 14.03.2018.
 */
public class MessageHandler  {
    private Socket socket;

    public MessageHandler (Socket socket) {
        this.socket = socket;
    }

    public void authorize() {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            UserMessage msg = null;
            //Диалог ввода логина и пароля
            try (Scanner sc = new Scanner(in)) {
                String login = "";
                String pass = "";
                System.out.println("Enter login:");
                if (sc.hasNextLine())
                    login = sc.nextLine();
                System.out.println("Enter your password:");
                if (sc.hasNextLine())
                    pass = sc.nextLine();
                User system = new User();
                system.setUserId("system");
                system.setLogin("system");
                system.setPassword("system");
                msg = new UserMessage("login="+login+";password="+pass,
                        system, LocalDate.now(), LocalTime.now());
            }
            try (ObjectInputStream din = new ObjectInputStream(in);
            ObjectOutputStream dot = new ObjectOutputStream(out)) {
                dot.writeObject(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
