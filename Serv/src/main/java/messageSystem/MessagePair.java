package messageSystem;

/**
 * Объект для хранения сообщения от конкретного хендлера
 * <p>
 * Created by Денис on 15.03.2018.
 */
public class MessagePair {
    private Message message;
    private int handlerId;

    public MessagePair(int handlerId, Message message) {
        this.handlerId = handlerId;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public int getHandlerId() {
        return handlerId;
    }
}
