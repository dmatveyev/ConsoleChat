package server.clientData;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Синглтон
 */
public class UsersManager {

    private static UsersManager usersManager;

    private Map<String,User> users;
    private Map<String,User> activeUsers;

    private UsersManager () {
        User admin = createAdmin();
        users = new ConcurrentHashMap<>();
        users.put("0",admin);
        activeUsers = new ConcurrentHashMap<>();

    }

    private User createAdmin() {
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("password");

        return admin;
    }

    public static UsersManager getInstance() {
        if (usersManager == null) {
            usersManager = new UsersManager();
        }
        return usersManager;
    }
    public User createUser(String id, String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setUserId(id);
        users.put(id,user);
        return user;
    }

    public boolean removeActiveUser (String session) {
        if(session != null){
            if(isActive(session)) {
                activeUsers.remove(session);
                return true;
            }
        } return false;
    }
    public boolean isActive(String session) {
        if (session!= null) {
            for(Map.Entry<String, User> entry: activeUsers.entrySet()) {
                if (entry.getKey().equals(session))
                    return true;
                else return false;
            }
        }
        return false;
    }
    public String isRegistered(String login) {
        for(Map.Entry<String, User> entry: users.entrySet()) {
            if (entry.getValue().getLogin().equals(login)) {
                return entry.getValue().getUserId();
            }
        }
        return null;
    }
    public User getRegisteredUser(String id) {
       return users.get(id);
    }

    public User getActiveUser(String id) {
        for (Map.Entry<String, User> entry: activeUsers.entrySet()) {
            if (entry.getValue().getUserId().equals(id)) {
                return entry.getValue();
            }
        } return null;
    }

}