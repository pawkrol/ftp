package pl.pawkrol.academic.ftp.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.message.MessageResponsePair;
import pl.pawkrol.academic.ftp.client.message.PWDMessage;
import pl.pawkrol.academic.ftp.client.message.USERMessage;
import pl.pawkrol.academic.ftp.client.session.User;
import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.common.utils.ListViewAppender;
import pl.pawkrol.academic.ftp.common.utils.LogWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pawkrol on 4/24/16.
 */
public class Controller implements Initializable{

    @FXML private ListView<String> localFilesView;
    @FXML private ListView<String> remoteFilesView;
    @FXML private ListView<LogWrapper> rawResponseView;
    @FXML private ListView<?> transferView;

    @FXML private TextField remoteDirField;
    @FXML private TextField localDirField;
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField loginField;

    @FXML private PasswordField passwordField;

    @FXML private Button connectButton;

    private boolean connected = false;
    private ConnectionManager connectionManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ListViewAppender.setListView(rawResponseView);
    }

    @FXML
    public void connectButtonAction(){
        if (!connected){
            try {
                connect();

                if (connectionManager != null){
                    connectionManager.getMessageProcessor()
                                        .registerMessageResponseListener(this::onAnyResponse);
                }

                connected = true;
                connectButton.setText("Disconnect");

            } catch (IOException e) {
                createWarningDialog("Connection error", "Cannot connect to the remote host");
            }
        } else {
            connectionManager.close();

            connected = false;
            connectButton.setText("Connect");
        }
    }

    public void onAnyResponse(MessageResponsePair messageResponsePair){
        Response response = messageResponsePair.getResponse();
        Message message = messageResponsePair.getMessage();

        switch (message.getCommand()){
            case "PASS":
                if (response.getCode() == 230){
                    connectionManager.getMessageProcessor().sendMessage(
                            new PWDMessage(), this::onInitRemoteDir
                    );
                }
                break;
        }
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    private void connect() throws IOException {
        boolean loginFilled = validate(loginField);
        boolean passwordFilled = validate(passwordField);
        boolean hostFilled = validate(hostField);
        boolean portFilled = validate(portField);

        if ( loginFilled && passwordFilled && hostFilled && portFilled ) {
            String address = hostField.getText();
            String login = loginField.getText();
            String password = passwordField.getText();
            int port = Integer.parseInt(portField.getText());

            User user = new User(login, password);
            connectionManager = new ConnectionManager(address, port, user);
        }

    }

    private void onInitRemoteDir(Response response){
        Platform.runLater(() ->
            remoteDirField.setText(
                    response.getMessage().replace("\"", "")
            )
        );
    }

    private boolean validate(TextField textField){
        if (textField.getText().isEmpty()){
            textField.setStyle("-fx-background-color: #F7D5D5;" +
                    "-fx-border-color: #FF8787;" +
                    "-fx-border-radius: 3%;");
            return false;
        } else {
            textField.setStyle(null);
            return true;
        }
    }

    private void createWarningDialog(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
