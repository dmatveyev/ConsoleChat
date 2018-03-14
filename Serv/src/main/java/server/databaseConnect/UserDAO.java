package server.databaseConnect;

import server.clientData.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements DAO<User> {
     private ConnectDB connectDB;

     public UserDAO (){
         connectDB = new ConnectDB();
     }

    @Override
    public User get(String id) {
         User user = new User();
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("select * from users where id = ?");
            st.setString(1, id);
            ResultSet res = st.executeQuery();
            res.next();
            user.setUserId(res.getString(1));
            user.setLogin(res.getString(2));
            user.setPassword(res.getString(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public String getUserId(String login, String password) {
        String userId = null;
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("select * from users where login = ? and password = ?");
            st.setString(1,login);
            st.setString(2, password);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                userId = res.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    public void insert(final User user) {

        try(Connection conn = connectDB.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("insert into users (id,login, password) values (?,?,?)");
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            //переписать создание сессии с использованием sessionDAO
            PreparedStatement session = conn.prepareStatement("insert into user_session" +
                    " (id, session) values (?,?)");
            session.setString(1, user.getUserId());
            session.setString(2, null);
            session.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(final User user) {

    }

    @Override
    public void delete(final String userId) {
        try (Connection conn = connectDB.getConnection()) {
            PreparedStatement st = conn.prepareStatement("delete from user_session where id = ?");
            st.setString(1, userId);
            PreparedStatement st2 = conn.prepareStatement("delete from users where id = ?");
            st2.setString(1, userId);
            st.executeUpdate();
            st2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}