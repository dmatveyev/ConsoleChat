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
            case "broadcast": {
                message = new Message(text,
                        userLogin,
                        LocalDate.now(),
                        LocalTime.now()
                );
                message.setMessageType(messageType);
                break;
            }
            case "auth": {
                message = new Message(text,
                        userLogin,
                        LocalDate.now(),
                        LocalTime.now()
                );
                message.setMessageType(messageType);
                break;
            }
            case "clearSession": {
                message = new Message(text,
                        userLogin,
                        LocalDate.now(),
                        LocalTime.now()
                );
                message.setMessageType(messageType);
                break;
            }
        }
        return  message;
    }
}
