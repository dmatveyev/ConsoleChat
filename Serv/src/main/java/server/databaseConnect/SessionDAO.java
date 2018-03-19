package server.databaseConnect;

import server.clientData.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import static server.Server.logger;


public class SessionDAO implements DAO<Session> {
    private final ConnectDB connectDB;

    public SessionDAO() {
        connectDB = new ConnectDB();
    }

    @Override
    public Session get(final String id) {
        Session session = null;
        try (Connection conn = connectDB.getConnection()) {
            final PreparedStatement st = conn.prepareStatement("select * from user_session" +
                    " where id = ?");
            st.setString(1, id);
            final ResultSet result = st.executeQuery();
            if (result.next())
                session = new Session(result.getString(1),
                        result.getString(2));
        } catch (final SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return session;
    }

    @Override
    public void insert(final Session t) {
    }

    @Override
    public void update(final Session t) {
        try (Connection conn = connectDB.getConnection()) {
            final PreparedStatement st = conn.prepareStatement("update user_session" +
                    " set session = ?" +
                    " where id = ?");
            st.setString(2, t.getUserId());
            st.setString(1, t.getName());
            st.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void delete(final String userId) {
    }
}