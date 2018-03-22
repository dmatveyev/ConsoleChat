package messageSystem;

import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

/**
 * Created by Денис on 20.03.2018.
 */
public class SimpleManager extends MessageManager {
    private MessagePair message;

    public SimpleManager(final UsersManager usersManager, final UserSessionManager sessionManager) {
        super(usersManager, sessionManager);
    }

    public void update(final MessagePair message) {
        this.message = message;
    }

    public MessagePair getMessage() {
        return message;
    }
}
