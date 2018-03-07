package server.clientData;

import java.util.Objects;

/**
 * Created by Денис on 07.03.2018.
 */
public class User  {
    private String userId;
    private String session;
    private String login;
    private String password;

    public User() {
        this.userId = null;
        this.login = null;
        this.password = null;
        this.session = null;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }



    public String getSession() {
        return session;
    }

    public void setSession(final String session) {
        this.session = session;
    }



    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return 7 * Objects.hashCode(getLogin()) +
                11 * Objects.hashCode(getPassword());
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;
        User other = (User) otherObject;
        return getLogin().equals(other.getLogin())&& getPassword().equals(other.getPassword());
    }

    @Override
    public String toString() {
        return "[login:" + getLogin() + ", password: " + getPassword() + "]";
    }
}
