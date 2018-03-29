package application.messageSystem;

/**
 * Created by Денис on 16.03.2018.
 */
public class SystemMessage extends Message {
    private final String command;
    private String resultMessage;

    SystemMessage(final String command) {
        this.command = command;
    }

    String getCommand() {
        return command;
    }

    void setResultMessage(final String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public String toString() {
        return "System Message: " + resultMessage;
    }
}
