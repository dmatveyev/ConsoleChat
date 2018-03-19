package messageSystem;


/**
 * Создает сообщение нужного типа.
 * Created by Денис on 16.03.2018.
 */
public class MessageFactory {
    public static Message createSystemMessage(final String command) {
        return new SystemMessage(command);
    }

    static Message createAuthMessage(final String userId, final String userLogin, final String userPassword) {
        return new AuthMessage(userId, userLogin, userPassword);
    }
}
