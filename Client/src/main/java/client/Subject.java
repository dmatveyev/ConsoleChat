package client;

public interface Subject {
   void registerObserver(Observer observer);
   void removeObservers(Observer observer);
   void notifyObservers();
}