package client;

import client.message.Message;

public class MessageManager implements Observer {
    private Message message;

    public MessageManager() {

    }

    @Override
    public void update(Message message) {
        this.message = message;
        display();
    }
    public void display() {
        System.out.println(message);
    }
}