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

    private ListView<String> view;
    private ObservableList<String> list;

    public TransferWatcher(ListView<String> view){
        this.view = view;
        this.list = FXCollections.observableArrayList();
        view.setItems(list);
    }

    public void watch(MessageResponsePair messageResponsePair){
        Message message = messageResponsePair.getMessage();
        Response response = messageResponsePair.getResponse();

        if (message instanceof TransferMessage){
            if (response.getCode() == 150){
                addEntry(message);
            }
        }
    }

    private void addEntry(Message message){
        Platform.runLater(() -> {
            list.add(formatMessage(message));
            view.scrollTo(list.size());
        });
    }

    private String formatMessage(Message message){
        if (message.getCommand().equals("LIST") ||
                message.getCommand().equals("RETR")) {
            return "[DOWNLOAD]\t" + message.getCommand() + "\t" + message.getParams();
        }

        return "?";
    }
}
