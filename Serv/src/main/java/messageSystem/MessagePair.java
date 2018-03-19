package messageSystem;

/**
 * Объект для хранения сообщения от конкретного хендлера
 * <p>
 * Created by Денис on 15.03.2018.
 */
public class MessagePair {
    private final Message message;
    private final int handlerId;

    public MessagePair(final int handlerId, final Message message) {
        this.handlerId = handlerId;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    int getHandlerId() {
        return handlerId;
    }
}
