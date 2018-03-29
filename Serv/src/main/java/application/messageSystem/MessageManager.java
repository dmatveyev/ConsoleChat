package application.messageSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import application.server.ClientHandler;
import application.server.clientData.Session;
import application.server.clientData.User;
import application.server.clientData.UserSessionManager;
import application.server.clientData.UsersManager;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Управляет пользовательскими сообщениями
 * Created by Денис on 15.03.2018.
 */
@Component
public class MessageManager implements ApplicationListener<MessageEvent>, Manager {
    private Logger logger = Logger.getLogger("Server");
    private final Map<Integer, ClientHandler> handlers;
    private final UsersManager usersManager;
    private final UserSessionManager sessionManager;

    @Autowired
    MessageManager(final UsersManager usersManager, final UserSessionManager sessionManager) {
        handlers = new ConcurrentHashMap<>();
        this.usersManager = usersManager;
        this.sessionManager = sessionManager;
    }

    /**
     * Обрабатывает сообщение, определяет его тип,
     * и включает дальнеющую логику обработки в зависимости от типа сообщения
     */
    public synchronized void DoMessage(final MessageEvent messageEvent) {
        final int handlerId = messageEvent.getHandlerId();
        final Message msg = messageEvent.getMessage();
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

    public void sendMessageToAll(final Message msg) {
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

    public synchronized void removeHandler(final int id) {
        handlers.remove(id);
    }

    @Override
    public void onApplicationEvent(MessageEvent messageEvent) {
        DoMessage(messageEvent);
    }
}
