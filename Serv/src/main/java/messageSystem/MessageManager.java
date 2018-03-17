package messageSystem;

import server.ClientHandler;
import server.clientData.Session;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 15.03.2018.
 */
public class MessageManager {
    private MessageFactory messageFactory;
    private MessagePair message;
    private ConcurrentHashMap<Integer, ClientHandler> handlers;
    private UsersManager usersManager;
    private UserSessionManager sessionManager;

    public MessageManager(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
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
        if (msg instanceof BroadcastMessage) {
              sendMessageToAll();
        }
        if (msg instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage)msg;
                User user = null;
                try {
                    user = usersManager.authorize(auth.getUserlogin(), auth.getUserPassword());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (user != null) {
                    Message authMessage = messageFactory.createAuthMessage(
                            user.getUserId(),
                            user.getLogin(),
                            user.getPassword());
                    handlers.get(handlerId).setUser(user);
                    handlers.get(handlerId).printMessage(authMessage);
                }else {
                    Message failedAuth = messageFactory.createAuthMessage(
                            null,
                            null,
                           null);
                    handlers.get(handlerId).printMessage(failedAuth);
                }
        }
        if (msg instanceof SystemMessage){
                User user = handlers.get(handlerId).getUser();
                if (user != null) {
                    Session ss = sessionManager.isActive(user);
                    ss.setName(null);
                    sessionManager.doUnactive(ss);
                }
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
