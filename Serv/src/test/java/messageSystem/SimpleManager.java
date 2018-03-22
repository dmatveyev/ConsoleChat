package messageSystem;

import server.clientData.UserSessionManager;
import server.clientData.UsersManager;

/**
 * Created by Денис on 20.03.2018.
 */
public class SimpleManager extends MessageManager {
    private MessageEvent message;

    public SimpleManager(final UsersManager usersManager, final UserSessionManager sessionManager) {
        super(usersManager, sessionManager);
    }

    public void update(final MessageEvent message) {
        this.message = message;
    }

    public MessageEvent getMessage() {
        return message;
    }
}
