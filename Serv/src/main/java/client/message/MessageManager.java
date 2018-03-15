package client.message;

import server.ClientHandler;

import java.util.ArrayList;

/**
 * Created by Денис on 15.03.2018.
 */
public class MessageManager {
    private Message message;
    private ArrayList<ClientHandler> handlers;

    public MessageManager() {
        handlers = new ArrayList<>();
    }

    public void update(Message message) {
        this.message = message;
        sendMessageToAll();
    }

    public void sendMessageToAll() {
        for (ClientHandler handler:handlers) {
            handler.printMessage(message);
        }
    }
    public void addHandler(ClientHandler clientHandler) {
        handlers.add(clientHandler);
    }
    public void removeHandler(ClientHandler clientHandler){
        handlers.remove(handlers);
    }
}
