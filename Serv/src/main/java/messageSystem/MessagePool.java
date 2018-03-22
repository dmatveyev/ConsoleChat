package messageSystem;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Создаёт очередь для обработки сообщений.
 * Оповещает Менеджера сообщений о новом поступившем ссообщении.
 * Created by Денис on 15.03.2018.
 */
@Service("messagePool")
public class MessagePool {
    private static MessagePool instance;
    private final List<MessageManager> messageManagers;

    private MessagePool() {
        messageManagers = new ArrayList<>();
    }

    public static MessagePool getInstance() {

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
