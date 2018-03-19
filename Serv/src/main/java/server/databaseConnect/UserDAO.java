package server.databaseConnect;

import server.clientData.User;

import java.sql.*;
import java.util.logging.Level;

import static server.Server.logger;

public class UserDAO implements DAO<User> {
    private final ConnectDB connectDB;

    public UserDAO() {
        connectDB = new ConnectDB();
    }


    @SuppressWarnings({"JDBCResourceOpenedButNotSafelyClosed", "resource"})
    @Override
    public User get(final String id) {
        final User user = new User();
        try (Connection conn = connectDB.getConnection()) {
            final PreparedStatement st = conn.prepareStatement("select * from users where id = ?");
            st.setString(1, id);
            final ResultSet res = st.executeQuery();
            res.next();
            user.setUserId(res.getString(1));
            user.setLogin(res.getString(2));
            user.setPassword(res.getString(3));

        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return user;
    }

    @SuppressWarnings({"JDBCResourceOpenedButNotSafelyClosed", "resource"})
    public String getUserId(final String login, final String password) {
        String userId = null;
        try (Connection conn = connectDB.getConnection()) {
            final PreparedStatement st = conn.prepareStatement("select * from users where login = ? and password = ?");
            st.setString(1, login);
            st.setString(2, password);
            final ResultSet res = st.executeQuery();
            if (res.next()) {
                userId = res.getString(1);
            }
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return userId;
    }

    @SuppressWarnings("JDBCResourceOpenedButNotSafelyClosed")
    @Override
    public void insert(final User t) {
        try (Connection conn = connectDB.getConnection()) {
            final PreparedStatement statement = conn.prepareStatement("insert into users (id,login, password) values (?,?,?)");
            statement.setString(1, t.getUserId());
            statement.setString(2, t.getLogin());
            statement.setString(3, t.getPassword());
            statement.executeUpdate();
            //переписать создание сессии с использованием sessionDAO
            final PreparedStatement session = conn.prepareStatement("insert into user_session" +
                    " (id, session) values (?,?)");
            session.setString(1, t.getUserId());
            session.setString(2, null);
            session.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void update(final User t) {
    }

    @SuppressWarnings("JDBCResourceOpenedButNotSafelyClosed")
    @Override
    public void delete(final String userId) {
        try (Connection conn = connectDB.getConnection()) {
            final PreparedStatement st = conn.prepareStatement("delete from user_session where id = ?");
            st.setString(1, userId);
            final PreparedStatement st2 = conn.prepareStatement("delete from users where id = ?");
            st2.setString(1, userId);
            st.executeUpdate();
            st2.executeUpdate();
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}