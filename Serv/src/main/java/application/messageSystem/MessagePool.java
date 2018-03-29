package application.messageSystem;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Создаёт очередь для обработки сообщений.
 * Оповещает Менеджера сообщений о новом поступившем ссообщении.
 * Created by Денис on 15.03.2018.
 */
@Service("messagePool")
public class MessagePool implements ApplicationContextAware {

    private final List<Manager> messageManagers;
    private ApplicationContext applicationContext;

    private MessagePool() {
        messageManagers = new ArrayList<>();
    }

    public void addMessage(final MessageEvent message) {
        applicationContext.publishEvent(message);
    }

    public void registerManager(final Manager messageManager) {
        messageManagers.add(messageManager);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
