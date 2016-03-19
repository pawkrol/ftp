package pl.pawkrol.academic.ftp.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pawkrol on 3/7/16.
 */
public class ServerLogger {

    private ListView<String> logger;
    private ObservableList<String> logs;

    public ServerLogger(ListView<String> logger){
        this.logger = logger;
        this.logs = FXCollections.observableArrayList();
    }

    public void log(String info){
        logs.add("[" + getTime() + "]  " + info);
        logger.setItems(logs);
        logger.scrollTo(logs.size());
    }

    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format( calendar.getTime() );
    }
}
