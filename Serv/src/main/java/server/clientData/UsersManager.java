package server.clientData;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Синглтон
 */
public class UsersManager {

    private static UsersManager usersManager;

    private Map<String,User> users;
    private UserSessionManager userSessionManager;


    private UsersManager () {
        userSessionManager = UserSessionManager.getInstance();
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
    public String registerUser(User user) {
        String id = user.getUserId();
        users.put(id, user);
        return id;
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

    public User deleteUser(String id) {
        return users.remove(id);
    }

    /**
     * Проверяет введенные данные и авторизует пользователя.
     * @param login Предполагаемый логин пользователя
     * @param password предполагаемый пароль пользователя.
     * @return Зарегистрированный или новый пользователь
     * @throws IOException Пробоасывается в случае, если есть активная сессия пользователя.
     */
    public User authorize(String login, String password) throws IOException {
        User user;
        String userId = usersManager.isRegistered(login, password);
        if (userId!= null) {
            user = usersManager.getRegisteredUser(userId);
            if (userSessionManager.isActive(user) == null) {
                userSessionManager.doActive(userId,
                        userSessionManager.createUserSession(user));
                return user;
            } else {
                throw new IOException("User allredy authorized");
            }
        }else {
            user = new User ();
            user.setLogin(login);
            user.setPassword(password);
            String userid = String.valueOf(Math.random());
            user.setUserId(userid);
            registerUser(user);
            users.put(userid, user);
            userSessionManager.doActive(user.getUserId(),
                    userSessionManager.createUserSession(user));
            return user;
        }
    }
}