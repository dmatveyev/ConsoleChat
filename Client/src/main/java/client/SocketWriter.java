package client;

import messageSystem.Message;

import java.io.*;


/**Отвечает за серриализацию и отправку объекта Message
 * Created by Денис on 06.03.2018.
 */
public class SocketWriter implements Observer {
    private ObjectOutputStream outputStream;
    private Message message;
    public SocketWriter(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }
    public void write() {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(final Message message) {
        this.message = message;
        write();
    }
}
