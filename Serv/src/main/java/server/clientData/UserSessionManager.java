package server.clientData;

import server.databaseConnect.ConnectDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 08.03.2018.
 */
public class UserSessionManager {
    private ConnectDB connectDB;
    private static UserSessionManager instance;
    private Map<String, String> userSession;

    private UserSessionManager() {
        connectDB = new ConnectDB();
        userSession = new ConcurrentHashMap<>();
    }

    public static UserSessionManager getInstance(){
        if(instance == null)
            instance = new UserSessionManager();
        return instance;
    }

    public String createUserSession (User user) {
        String session = user.getUserId().concat(user.getLogin()).concat(user.getPassword());
        return session;
    }

    /**
     * Проверяет есть ли активный пользователь под этой сессией
     * @param user пользователь для проверки
     * @return String сессия пользователя, если есть активный пользователь. Null, если нет активного пользователя.
     */
    public String isActive(User user) {
        String session = "";
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("select session from user_session" +
                    " where id = ?");
            st.setString(1, user.getUserId());
            ResultSet result = st.executeQuery();
            result.next();
            session= result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return session;
    }

    public boolean doActive(String userId, String session) {
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("update user_session" +
                    " set session = ?" +
                    " where id = ?");
            st.setString(2,userId);
            st.setString(1, session);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean doUnactive(String userId) {
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("update user_session" +
                    " set session = null" +
                    " where id = ?");
            st.setString(1,userId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
