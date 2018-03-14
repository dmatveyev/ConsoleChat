package client;

import client.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**Принимает входящее сообщение  дессериализует
 * Created by Денис on 06.03.2018.
 */
public class SocketReader implements Runnable {
    private InputStream in;
    public SocketReader(InputStream in) {
        this.in = in;
    }
    @Override
    public void run() {
        try(ObjectInputStream oin = new ObjectInputStream(in)) {
            //Не уверен в условии чтения.
            while (true) {
                Message message = (Message) oin.readObject();
                System.out.println(message.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
