package messageSystem;

import java.io.Serializable;

/**
 * Created by Денис on 14.03.2018.
 */
public class User {

        private String userId;
        private String login;
        private String password;

        public User() {
            this.userId = null;
            this.login = null;
            this.password = null;
        }

    public User(String userId, String login, String password) {
            this.userId = userId;
            this.login = login;
            this.password = password;
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
}
