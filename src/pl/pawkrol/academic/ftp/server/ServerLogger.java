package pl.pawkrol.academic.ftp.server;

import javafx.scene.control.TextArea;

/**
 * Created by pawkrol on 3/7/16.
 */
public class ServerLogger {

    private TextArea logger;

    public ServerLogger(TextArea logger){
        this.logger = logger;
    }

    public void log(String info){
        logger.setText(info);
    }

}
