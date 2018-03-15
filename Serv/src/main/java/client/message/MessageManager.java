package client.message;

import server.ClientHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 15.03.2018.
 */
public class MessageManager {
    private MessagePair message;
    private ConcurrentHashMap<Integer, ClientHandler> handlers;


    public MessageManager() {
        handlers = new ConcurrentHashMap<>();
    }

    /**
     * Обрабатывает сообщение, определяет его тип,
     * и включает дальнеющую логику обработки в зависимости от типа сообщения
     */
    private void DoMessage() {
        sendMessageToAll();
    }

    public void update(MessagePair message) {
        this.message = message;
        DoMessage();
    }

    public void sendMessageToAll() {
        for (Map.Entry<Integer, ClientHandler> handler: handlers.entrySet()) {
            handler.getValue().printMessage(message.getMessage());
        }
    }
    public void addHandler(int id, ClientHandler clientHandler) {
        handlers.put(id,clientHandler);
    }
    public void removeHandler(int id){
        handlers.remove(id);
    }
}
