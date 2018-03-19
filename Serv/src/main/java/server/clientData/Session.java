package server.clientData;

public class Session {
    private String name;
    private final String userId;

    public Session(final String userId, final String name) {
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