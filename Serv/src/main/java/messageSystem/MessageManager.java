package messageSystem;

import server.ClientHandler;
import server.clientData.Session;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static server.Server.logger;

/**
 * Управляет пользовательскими сообщениями
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
    private synchronized void DoMessage(MessagePair message) {
        int handlerId = message.getHandlerId();
        Message msg = message.getMessage();
        if (msg instanceof BroadcastMessage) {
            sendMessageToAll(msg);
        }
        if (msg instanceof AuthMessage) {
            AuthMessage auth = (AuthMessage) msg;
            User user = null;
            try {
                user = usersManager.authorize(auth.getUserLogin(), auth.getUserPassword());
            } catch (IOException e) {
                logger.log(Level.WARNING, e.getStackTrace().toString());
            }
            if (user != null) {
                Message authMessage = messageFactory.createAuthMessage(
                        user.getUserId(),
                        user.getLogin(),
                        user.getPassword());
                handlers.get(handlerId).setUser(user);
                handlers.get(handlerId).printMessage(authMessage);
            } else {
                Message failedAuth = messageFactory.createAuthMessage(
                        null,
                        null,
                        null);
                handlers.get(handlerId).printMessage(failedAuth);
                handlers.remove(handlerId);
            }
        }
        if (msg instanceof SystemMessage) {
            SystemMessage sys = (SystemMessage) msg;
            String command = sys.getCommand();
            if (command.equals("clearSession")) {
                User user = handlers.get(handlerId).getUser();
                if (user != null) {
                    Session ss = sessionManager.isActive(user);
                    if (ss != null) {
                        ss.setName(null);
                        sessionManager.doUnactive(ss);
                        sys.setResultMessage("Session for user " + user.getLogin() + " was cleared successfully");
                        logger.log(Level.INFO, "{0} {1}",
                                new Object[]{this.getClass().getSimpleName(), sys});
                        removeHandler(handlerId);
                    }
                }
            }
        }
    }

    public synchronized void update(MessagePair message) {
        DoMessage(message);
    }

    public synchronized void sendMessageToAll(Message msg) {
        if (handlers.size() != 0) {
            for (Map.Entry<Integer, ClientHandler> handler : handlers.entrySet()) {
                logger.log(Level.FINE, "{0} sending message to {1}",
                        new Object[]{this.getClass().getSimpleName(),
                                handler.getValue().getUser().getLogin()});
                handler.getValue().printMessage(msg);
            }
        }
    }

    public synchronized void addHandler(int id, ClientHandler clientHandler) {
        handlers.put(id, clientHandler);
    }

    public synchronized void removeHandler(int id) {
        handlers.remove(id);
    }
}
