package server.clientData;

public class Session {
    private String name;
    private String userId;

    public Session(String userId, String name) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }
}