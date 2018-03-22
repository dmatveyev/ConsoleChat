package readers;

public interface ClientSubject {
    void registerObserver(ClientObserver observer);

    void removeObserver(ClientObserver observer);

    void notifyObservers();

    void run();
}