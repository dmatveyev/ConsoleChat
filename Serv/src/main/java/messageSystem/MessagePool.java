package messageSystem;

import java.util.ArrayList;
import java.util.List;


/**
 * Создаёт очередь для обработки сообщений.
 * Оповещает Менеджера сообщений о новом поступившем ссообщении.
 * Created by Денис on 15.03.2018.
 */
public class MessagePool {
    private static MessagePool instance;
    private final List<MessageManager> messageManagers;

    private MessagePool() {
        messageManagers = new ArrayList<>();
    }

    public static MessagePool getInstance() {
        if (instance == null)
            instance = new MessagePool();
        return instance;
    }

    public void addMessage(final MessagePair message) {
        notifyManagers(message);
    }

    public void registerManager(final MessageManager messageManager) {
        messageManagers.add(messageManager);
    }

    private void notifyManagers(final MessagePair message)  {
        for (final MessageManager messageManager : messageManagers) {
            messageManager.update(message);
        }
    }
}
