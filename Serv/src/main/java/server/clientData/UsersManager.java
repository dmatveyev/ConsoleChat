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
        addActiveUser(admin);
    }

    private User createAdmin() {
        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword("password");
        admin.setSession("adminsession");
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
    public void createUserSession(User user) {
        ;
        if (user != null) {
            String session =user.getLogin().concat(user.getPassword());
            user.setSession(session);
        }
    }
    public void removeUserSession (User user) {
        if(user!=null) {
            user.setSession(null);
        }
    }

    public boolean addActiveUser (User user) {
        if(user != null && user.getSession()!= null) {
            activeUsers.put(user.getSession(), user);
            return true;
        } else return  false;
    }
    public boolean removeActiveUser (User user) {
        if(user != null){
            if(isActive(user)) {
                activeUsers.remove(user.getSession());
                return true;
            }
        } return false;
    }
    public boolean isActive(User user) {
        if (user!= null) {
            for(Map.Entry<String, User> entry: activeUsers.entrySet()) {
                if (entry.getKey().equals(user.getSession()))
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

}