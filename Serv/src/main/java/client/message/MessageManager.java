package client.message;

import javafx.util.Pair;
import server.ClientHandler;
import server.clientData.Session;
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
    private UsersManager usersManager;
    private UserSessionManager sessionManager;

    public MessageManager() {
        handlers = new ConcurrentHashMap<>();
        usersManager = UsersManager.getInstance();
        sessionManager = UserSessionManager.getInstance();
    }

    /**
     * Обрабатывает сообщение, определяет его тип,
     * и включает дальнеющую логику обработки в зависимости от типа сообщения
     */
    private void DoMessage() {
        int handlerId = message.getHandlerId();
        Message msg = message.getMessage();
        String msgType  = msg.getMessageType();
        switch (msgType) {
            case "broadcast":sendMessageToAll();
            break;
            case "auth": {
                String[] creds = msg.getText().split(":");
                User user = null;
                try {
                    user = usersManager.authorize(creds[0], creds[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (user != null) {
                    Message authMessage = new Message(user.getUserId(), user.getLogin(),
                            LocalDate.now(), LocalTime.now());
                    authMessage.setMessageType("auth");
                    handlers.get(handlerId).setUser(user);
                    handlers.get(handlerId).printMessage(authMessage);
                }else {
                    Message failedAuth = new Message(
                            "User allready loggined",
                            "system",
                            LocalDate.now(),
                            LocalTime.now()
                    );
                    msg.setMessageType("auth");
                    handlers.get(handlerId).printMessage(failedAuth);
                }
            } break;
            case "clearSession": {
                User user = handlers.get(handlerId).getUser();
                if (user != null) {
                    Session ss = sessionManager.isActive(user);
                    ss.setName(null);
                    sessionManager.doUnactive(ss);
                }
            }break;

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
