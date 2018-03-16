package client;


import client.message.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**Реализация клиента
 * Created by Денис on 06.03.2018.
 */
public class Client {
    Socket clientS;
    public Client(int port) {
        try {
            User user = new User();
            clientS = new Socket("localhost", port);
            ObjectInputStream in = new ObjectInputStream(clientS.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientS.getOutputStream());
            MessageManager manager = new MessageManager(user);
            SocketReader reader =  new SocketReader(in);
            SocketWriter writer =  new SocketWriter(out);
            UserMessageReader userMessageReader = new UserMessageReader(user);
            manager.registerObserver(writer);
            userMessageReader.registerObserver(manager);
            reader.registerObserver(manager);
            Thread read = new Thread (reader);
            read.start();
            userMessageReader.read();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (e.getMessage().contains("refused"))
                System.err.println("Connetcion Refused");
            else e.printStackTrace();
        }
        finally {
            try {
                clientS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
