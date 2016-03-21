package pl.pawkrol.academic.ftp.server.utils;

import org.apache.logging.log4j.Level;

/**
 * Created by Pawel on 2016-03-21.
 */
public class LogWrapper {
    private String message;
    private Level level;

    public LogWrapper(String message, Level level) {
        this.message = message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
