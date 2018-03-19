package messageSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Создаёт очередь для обработки сообщений.
 * Оповещает Менеджера сообщений о новом поступившем ссообщении.
 * Created by Денис on 15.03.2018.
 */
public class MessagePool {
    private static MessagePool instance;
    private final List<MessageManager> messageManagers;
    private final BlockingQueue<MessagePair> queue;

    private MessagePool() {
        queue = new ArrayBlockingQueue<>(100);
        messageManagers = new ArrayList<>();
    }

    public static MessagePool getInstance() {
        if (instance == null)
            instance = new MessagePool();
        return instance;
    }

    public void addMessage(final MessagePair message) {
        try {
            queue.put(message);
            notifyManagers();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MessagePair getMessage() {
        try {
            return queue.take();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerManager(final MessageManager messageManager) {
        messageManagers.add(messageManager);
    }

    private void notifyManagers() {
        for (final MessageManager messageManager : messageManagers) {
            messageManager.update(getMessage());
        }
    }
}
