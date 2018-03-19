package server.clientData;

import server.databaseConnect.SessionDAO;

/**
 * Управляет сессиями пользователей
 * Created by Денис on 08.03.2018.
 */
public class UserSessionManager {
    private final SessionDAO sessionDAO;
    private static UserSessionManager instance;


    private UserSessionManager() {
        sessionDAO = new SessionDAO();
    }

    public static UserSessionManager getInstance() {
        if (instance == null)
            instance = new UserSessionManager();
        return instance;
    }

    static Session createUserSession(final User user) {
        final String session = user.getUserId().concat(user.getLogin()).concat(user.getPassword());
        return new Session(user.getUserId(), session);
    }

    /**
     * Проверяет есть ли активный пользователь под этой сессией
     *
     * @param user пользователь для проверки
     * @return Объект сессии пользователя, если есть активный пользователь. Null, если нет активного пользователя.
     */
    public Session isActive(final User user) {
        return sessionDAO.get(user.getUserId());

    }

    /**
     * Активизирует сессия для покльзователя
     *
     * @param session Объект сессии
     *
     */
    void doActive(final Session session) {
        sessionDAO.update(session);
    }

    /**
     * Удаляет сессию для поользователя
     *
     * @param session Объект сессии
     *
     */
    public void Unactivated(final Session session) {
        sessionDAO.update(session);
    }

}
