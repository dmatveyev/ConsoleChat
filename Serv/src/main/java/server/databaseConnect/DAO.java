package server.databaseConnect;

public interface DAO <T> {
    T get(String id);
    void insert(T t);
    void update(T t);
    void delete(String userId);
}