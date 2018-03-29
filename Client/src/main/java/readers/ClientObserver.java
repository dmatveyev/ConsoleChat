package readers;

import application.messageSystem.Message;

public interface ClientObserver {
    void updateClient(Message message);
}