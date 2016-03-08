package pl.pawkrol.academic.ftp.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private ConnectionManager connectionManager;
    private ServerLogger serverLogger;

    @FXML
    Button button;

    @FXML
    TextArea logger; //TODO: change

    @FXML
    Label portLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        serverLogger = new ServerLogger(logger);
    }

    public void setConnectionManager(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    @FXML
    private void onClickButton(ActionEvent actionEvent){
        if (connectionManager != null){
            if (button.getText().equals("Start")) {
                serverLogger.log("Server started");
                connectionManager.run();
                button.setText("Stop");

                portLabel.setText("" + connectionManager.getPort());
            } else {
                serverLogger.log("Server stopped");
                connectionManager.stop();
                button.setText("Start");

                portLabel.setText("null");
            }
        }
    }

}