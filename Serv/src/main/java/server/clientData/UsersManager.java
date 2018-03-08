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


    private UsersManager () {
        User admin = createAdmin();
        users = new ConcurrentHashMap<>();
        users.put("0",admin);
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

    /**
     * Проверяет совпадение пользователя в списке зарегистрированных
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Id пользователя, если такой пользователь был найден
     */
    public String isRegistered(String login, String password) {
        for(Map.Entry<String, User> entry: users.entrySet()) {
            if (entry.getValue().getLogin().equals(login)
                    && entry.getValue().getPassword().equals(password)) {
                return entry.getValue().getUserId();
            }
        }
        return null;
    }
    public User getRegisteredUser(String id) {
       return users.get(id);
    }



}