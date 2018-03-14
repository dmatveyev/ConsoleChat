package client;

import client.message.User;
import client.message.UserMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**Отвечает за серриализацию и отправку объекта UserMessage
 * Created by Денис on 06.03.2018.
 */
public class SocketWriter implements Runnable {
    Socket clientS;
    public SocketWriter(Socket clientS) {
        this.clientS = clientS;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(System.in);
             ObjectOutputStream out = new ObjectOutputStream(clientS.getOutputStream())){
             String text;
             while(in.hasNextLine()){
                text = in.nextLine();
                User user = new User (String.valueOf(Math.random()), "default", "default");
                UserMessage message = new UserMessage(text, user, LocalDate.now(), LocalTime.now());
                out.writeObject(message);
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
