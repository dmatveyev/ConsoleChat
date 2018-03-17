package messageSystem;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class MessageFactory {

    public Message createMessage(String messageType, String text, String userLogin){
        Message message = null;
        switch (messageType) {
           case "clearSession": {
                message = new SystemMessage(text,
                        userLogin,
                        LocalDate.now(),
                        LocalTime.now()
                );
                break;
            }
        }
        return  message;
    }
    public Message createBroadcastMessage(String text, String userName){
        Message message = new BroadcastMessage(text,
                userName);
        return message;
    }
    public Message createAuthMessage (String userId, String userLogin, String userPassword){
        Message message =  new AuthMessage(userId, userLogin, userPassword);
        return message;
    }
}
