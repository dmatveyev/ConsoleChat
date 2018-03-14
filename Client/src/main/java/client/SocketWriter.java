package client;

import client.message.User;
import client.message.Message;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**Отвечает за серриализацию и отправку объекта client.message.Message
 * Created by Денис on 06.03.2018.
 */
public class SocketWriter implements Runnable {
    OutputStream outputStream;
    public SocketWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(System.in);
             ObjectOutputStream out = new ObjectOutputStream(outputStream))
             {
             String text;
             while (true) {
                 if (in.hasNextLine()) {
                     System.out.println("reading...");
                     text = in.nextLine();
                     User user = new User(String.valueOf(Math.random()), "default", "default");
                     Message message = new Message(text, user.getUserId(), LocalDate.now(), LocalTime.now());
                     System.out.println ("sending message");
                     out.writeObject(message);
                     out.flush();
                     System.out.println ("message sent");
                 }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
