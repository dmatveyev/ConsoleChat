package messageSystem;

import org.springframework.context.ApplicationEvent;
import server.ClientHandler;

/**
 * Объект для хранения сообщения от конкретного хендлера
 * <p>
 * Created by Денис on 15.03.2018.
 */
public class MessageEvent extends ApplicationEvent {
    private final Message message;
    private final ClientHandler handler;


    public MessageEvent(final ClientHandler handler, final Message message) {
        super(handler);
        this.handler = handler;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    int getHandlerId() {
        return handler.getHandlerId();
    }
}
