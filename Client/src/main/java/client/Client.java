package client;

import client.message.Message;

import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;


/**Реализация клиента
 * Created by Денис on 06.03.2018.
 */
public class Client {

    public Client(int port) {
        try {
            Socket clientS = new Socket("localhost", port);
            MessageManager manager = new MessageManager();
            SocketReader reader =  new SocketReader(clientS.getInputStream());
            SocketWriter writer =  new SocketWriter(clientS.getOutputStream());
            reader.registerObserver(manager);
            Thread read = new Thread (reader);
            Thread write = new Thread (writer);
            read.start();
            write.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (e.getMessage().contains("refused"))
                System.err.println("Connetcion Refused");
            else e.printStackTrace();

        }
    }
}
