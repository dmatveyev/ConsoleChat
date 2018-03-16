package client;

import messageSystem.Message;

public interface Observer {
    void update(Message message);
}