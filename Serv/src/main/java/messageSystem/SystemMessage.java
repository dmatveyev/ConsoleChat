package messageSystem;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Денис on 16.03.2018.
 */
public class SystemMessage extends Message {
    private String userName = "system";
    private String command;
    private String resultMessage;
    private LocalDate date;
    private LocalTime time;

    public SystemMessage (String comamand) {
        this.command = comamand;
    }

    public String getCommand() {
        return command;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public String toString() {
        return "System Message: " + resultMessage;
    }
}
