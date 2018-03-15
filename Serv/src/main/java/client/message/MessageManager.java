package client.message;

import server.ClientHandler;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
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
        int handlerId = message.getHandlerId();
        Message msg = message.getMessage();
        String msgType  = msg.getMessageType();
        if (msgType.equals("broadcast"))
            sendMessageToAll();
        if(msgType.equals("auth")){
            UsersManager usersManager = UsersManager.getInstance();
            String[] creds = msg.getText().split(":");
            User user = null;
            try {
                user = usersManager.authorize(creds[0], creds[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message authMessage = new Message(user.getUserId(),user.getLogin(),
                    LocalDate.now(), LocalTime.now());
            authMessage.setMessageType("auth");
            handlers.get(handlerId).printMessage(authMessage);
        }
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
