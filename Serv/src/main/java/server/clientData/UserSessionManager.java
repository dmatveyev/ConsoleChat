package server.clientData;

import server.databaseConnect.SessionDAO;

/**
 * Created by Денис on 08.03.2018.
 */
public class UserSessionManager {
    private SessionDAO sessionDAO;
    private static UserSessionManager instance;


    private UserSessionManager() {
        sessionDAO = new SessionDAO();
    }

    public static UserSessionManager getInstance(){
        if(instance == null)
            instance = new UserSessionManager();
        return instance;
    }

    public Session createUserSession (User user) {
        String session = user.getUserId().concat(user.getLogin()).concat(user.getPassword());
        return new Session(user.getUserId(), session);
    }

    /**
     * Проверяет есть ли активный пользователь под этой сессией
     * @param user пользователь для проверки
     * @return Объект сессии пользователя, если есть активный пользователь. Null, если нет активного пользователя.
     */
    public Session isActive(User user) {
        return sessionDAO.get(user.getUserId());

    }

    public boolean doActive(Session session) {
        sessionDAO.update(session);
        return true;
    }

    public boolean doUnactive(Session session) {
        sessionDAO.update(session);
        return true;
    }

}
