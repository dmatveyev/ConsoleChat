package server.clientData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 08.03.2018.
 */
public class UserSessionManager {
    private static UserSessionManager instance;
    private Map<String, String> userSession;

    private UserSessionManager() {
        userSession = new ConcurrentHashMap<>();
    }

    public static UserSessionManager getInstance(){
        if(instance == null)
            instance = new UserSessionManager();
        return instance;
    }

    public String createUserSession (User user) {
        return user.getUserId().concat(user.getLogin()).concat(user.getPassword());
    }

    /**
     * Проверяет есть ли активный пользователь под этой сессией
     * @param user пользователь для проверки
     * @return String сессия пользователя, если есть активный пользователь. Null, если нет активного пользователя.
     */
    public String isActive(User user) {
        for(Map.Entry<String, String> entry: userSession.entrySet()) {
            if ( entry.getKey().equals(user.getUserId()))
                return entry.getValue();
        }
        return "";
    }

    public boolean doActive(String userId, String session) {
        userSession.put(userId, session );
        return true;
    }

    public boolean doUnactive(String userId) {
        userSession.put(userId, "");
        return true;
    }

}
