package messageSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.ClientHandler;
import server.clientData.Session;
import server.clientData.User;
import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Управляет пользовательскими сообщениями
 * Created by Денис on 15.03.2018.
 */
@Service("messageManager")
public class MessageManager {
    private Logger logger = Logger.getLogger("Server");
    private final Map<Integer, ClientHandler> handlers;
    private final UsersManager usersManager;
    private final UserSessionManager sessionManager;

    @Autowired
    public MessageManager( final UsersManager usersManager, final UserSessionManager sessionManager) {
        handlers = new ConcurrentHashMap<>();
        this.usersManager = usersManager;
        this.sessionManager = sessionManager;
    }

    /**
     * Обрабатывает сообщение, определяет его тип,
     * и включает дальнеющую логику обработки в зависимости от типа сообщения
     */
    private synchronized void DoMessage(final MessagePair messagePair) {
        final int handlerId = messagePair.getHandlerId();
        final Message msg = messagePair.getMessage();
        if (msg instanceof BroadcastMessage) {
            sendMessageToAll(msg);
        }
        if (msg instanceof AuthMessage) {
            final AuthMessage auth = (AuthMessage) msg;
            User user = null;
            try {
                user = usersManager.authorize(auth.getUserLogin(), auth.getUserPassword());
            } catch (final IOException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
            if (user != null) {
                final Message authMessage = MessageFactory.createAuthMessage(
                        user.getUserId(),
                        user.getLogin(),
                        user.getPassword());
                handlers.get(handlerId).setUser(user);
                handlers.get(handlerId).printMessage(authMessage);
            } else {
                final Message failedAuth = MessageFactory.createAuthMessage(
                        null,
                        null,
                        null);
                handlers.get(handlerId).printMessage(failedAuth);
                handlers.remove(handlerId);
            }
        }
        if (msg instanceof SystemMessage) {
            final SystemMessage sys = (SystemMessage) msg;
            final String command = sys.getCommand();
            if (command.equals("clearSession")) {
                final User user = handlers.get(handlerId).getUser();
                if (user != null) {
                    final Session ss = sessionManager.getSession(user);
                    if (ss != null) {
                        ss.setName(null);
                        sessionManager.Unactivated(user);
                        sys.setResultMessage("Session for user " + user.getLogin() + " was cleared successfully");
                        logger.log(Level.INFO, "{0} {1}",
                                new Object[]{this.getClass().getSimpleName(), sys});
                        removeHandler(handlerId);
                    }
                }
            }
        }
    }

    synchronized void update(final MessagePair messagePair) {
        DoMessage(messagePair);
    }

    private synchronized void sendMessageToAll(final Message msg) {
        if (!handlers.isEmpty()) {
            for (final Map.Entry<Integer, ClientHandler> handler : handlers.entrySet()) {
                logger.log(Level.FINE, "{0} sending message to {1}",
                        new Object[]{this.getClass().getSimpleName(),
                                handler.getValue().getUser().getLogin()});
                handler.getValue().printMessage(msg);
            }
        }
    }

    public synchronized void addHandler(final int id, final ClientHandler clientHandler) {
        handlers.put(id, clientHandler);
    }

    private synchronized void removeHandler(final int id) {
        handlers.remove(id);
    }
}
