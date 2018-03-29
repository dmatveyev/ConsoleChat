package readers;

import application.messageSystem.Message;

public interface Subject {
   void registerObserver(Observer observer);
   void removeObservers(Observer observer);
   void notifyObservers(final Message message);
}