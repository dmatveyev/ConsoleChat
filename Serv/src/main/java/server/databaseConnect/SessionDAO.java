package server.databaseConnect;

import server.clientData.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import static server.Server.logger;

public class SessionDAO implements DAO<Session> {
    private ConnectDB connectDB;

    public SessionDAO() {
        connectDB = new ConnectDB();
    }

    @Override
    public Session get(final String userId) {
        Session session = null;
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("select * from user_session" +
                    " where id = ?");
            st.setString(1, userId);
            ResultSet result = st.executeQuery();
            if (result.next())
                session = new Session(result.getString(1),
                        result.getString(2));
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getStackTrace().toString());
        }
        return session;
    }

    @Override
    public void insert(final Session session) {
    }

    @Override
    public void update(final Session session) {
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("update user_session" +
                    " set session = ?" +
                    " where id = ?");
            st.setString(2, session.getUserId());
            st.setString(1, session.getName());
            st.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getStackTrace().toString());
        }
    }

    @Override
    public void delete(final String userId) {
    }
}