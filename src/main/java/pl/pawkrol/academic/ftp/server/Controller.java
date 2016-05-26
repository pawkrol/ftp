package pl.pawkrol.academic.ftp.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.common.utils.ListViewAppender;
import pl.pawkrol.academic.ftp.common.utils.LogWrapper;

import javax.swing.event.DocumentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private ConnectionManager connectionManager;
    private static final Logger log = LogManager.getLogger("logger");

    @FXML
    Button button;

    @FXML
    ListView<LogWrapper> logger;

    @FXML
    Label portLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ListViewAppender.setListView(logger);
    }

    public void setConnectionManager(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    @FXML
    private void onClickButton(ActionEvent actionEvent) throws IOException{
        if (connectionManager != null){
            if (button.getText().equals("Start")) {
                connectionManager.run();

                log.log(Level.INFO, "Server started");
                button.setText("Stop");
                portLabel.setText("" + connectionManager.getPort());
            } else {
                connectionManager.close();

                log.log(Level.INFO, "Server stopped");
                button.setText("Start");
                portLabel.setText("null");
            }
        }
    }


}