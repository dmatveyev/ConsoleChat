package messageSystem;

/**
 * Created by Денис on 20.03.2018.
 */
public class SimpleManager extends MessageManager {
    private MessagePair message;

    public void update(final MessagePair message) {
        this.message = message;
    }

    public MessagePair getMessage() {
        return message;
    }
}
