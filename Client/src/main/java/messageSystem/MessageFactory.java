package messageSystem;



/**
 * Created by Денис on 16.03.2018.
 */
public class MessageFactory {

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
