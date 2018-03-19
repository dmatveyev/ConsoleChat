package server.clientData;

import org.omg.PortableServer.ServantRetentionPolicy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Денис on 07.03.2018.
 */
public class User implements Serializable {
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
        return getLogin().equals(other.getLogin()) && getPassword().equals(other.getPassword());
    }

    @Override
    public String toString() {
        return "[login:" + getLogin() + ", password: " + getPassword() + "]";
    }
}
