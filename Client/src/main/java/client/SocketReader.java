package client;

import client.message.UserMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**Принимает входящее сообщение  дессериализует
 * Created by Денис on 06.03.2018.
 */
public class SocketReader implements Runnable {
    private Socket clientS;
    public SocketReader(Socket clientS) {
        this.clientS = clientS;
    }
    @Override
    public void run() {
        try(ObjectInputStream in = new ObjectInputStream(clientS.getInputStream())) {
            //Не уверен в условии чтения.
            while (in.available()> 0) {
                UserMessage message = (UserMessage) in.readObject();
                System.out.println(message.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
