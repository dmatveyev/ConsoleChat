package server.clientData;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Денис on 07.03.2018.
 */
public class User  {
    private String userId;
    private String login;
    private String password;

    public User() {
        this.userId = null;
        this.login = null;
        this.password = null;
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


    public void setLogin(final String login) {
        this.login = login;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return (7 * Objects.hashCode(getLogin())) +
                (11 * Objects.hashCode(getPassword()));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final User other = (User) obj;
        return getLogin().equals(other.getLogin()) && getPassword().equals(other.getPassword());
    }

    @Override
    public String toString() {
        return "[login:" + getLogin() + ", password: " + getPassword() + "]";
    }
}
