package readers;

import application.messageSystem.Message;

public interface Observer {
    void update(Message message);
}