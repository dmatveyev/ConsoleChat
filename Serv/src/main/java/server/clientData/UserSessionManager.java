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
    public String isActive(String id) {
        for(Map.Entry<String, String> entry: userSession.entrySet()) {
            if ( entry.getValue().equals(id))
                return  entry.getKey();
        }
        return null;
    }

    public boolean doActive(String session, String id) {
        userSession.put(session, id );
        return true;
    }

    public boolean doUnactive(String session) {
        userSession.remove(session);
        return true;
    }

}
