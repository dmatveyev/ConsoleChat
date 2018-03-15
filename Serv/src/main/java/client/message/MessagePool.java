package client.message;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;


/**Создаёт очередь для обработки сообщений.
 *  Оповещает Менеджера сообщений о новом поступившем ссообщении.
 * Created by Денис on 15.03.2018.
 */
public class MessagePool {
    private static MessagePool instance;
    private ArrayList<MessageManager> messageManagers;
    private ArrayBlockingQueue<MessagePair> queue;

    private MessagePool() {
        queue = new ArrayBlockingQueue<>(100);
        messageManagers = new ArrayList<>();
    }

    public static MessagePool getInstance(){
        if (instance == null)
            instance = new MessagePool();
        return instance;
    }

    public void addMessage (MessagePair message){
        try {
            queue.put(message);
            notifyManagers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MessagePair getMessage ()  {
        try {
            return  queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerManager (MessageManager messageManager) {
        messageManagers.add(messageManager);
    }
    public void removeManager(MessageManager messageManager) {
        messageManagers.remove(messageManager);
    }

    private void notifyManagers () {
        for(MessageManager messageManager: messageManagers) {
            messageManager.update(getMessage());
        }
    }
}
