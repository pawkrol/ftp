package pl.pawkrol.academic.ftp.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.message.MessageResponsePair;
import pl.pawkrol.academic.ftp.client.message.transfer.TransferMessage;
import pl.pawkrol.academic.ftp.common.Response;

/**
 * Created by pawkrol on 5/28/16.
 */
public class TransferWatcher {

    public enum Type{
        DOWNLOAD("[DOWNLOAD]"),
        UPLOAD("[UPLOAD]");

        private String name;

        Type(String name){
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private ListView<String> view;
    private ObservableList<String> list;

    public TransferWatcher(ListView<String> view){
        this.view = view;
        this.list = FXCollections.observableArrayList();
        view.setItems(list);
    }

    public void addEntry(Type type, String filename, long time, int bytes){
        Platform.runLater(() -> {
            list.add(formatMessage(type, filename, time, bytes));
            view.setItems(list);
            view.scrollTo(list.size());
        });
    }

    public void clear(){
        list.clear();
        Platform.runLater(() -> view.setItems(FXCollections.emptyObservableList()));
    }

    private String formatMessage(Type type, String filename, long time, int bytes){
        if (time != 0) {
            float speed = bytes / time;
            return String.format("%s\t\"%s\"\t%d bytes in %d ms <%.2f>bytes/ms",
                    type, filename, bytes, time, speed);
        } else {
            return String.format("%s\t\"%s\"\t%d bytes in %d ms",
                    type, filename, bytes, time);
        }
    }
}
