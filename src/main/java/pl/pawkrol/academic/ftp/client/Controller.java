package pl.pawkrol.academic.ftp.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;

import java.io.IOException;

/**
 * Created by pawkrol on 4/24/16.
 */
public class Controller {

    @FXML private ListView<String> localFilesView;
    @FXML private ListView<String> remoteFilesView;
    @FXML private ListView<String> rawResponseView;
    @FXML private ListView<?> transferView;

    @FXML private TextField remoteDirField;
    @FXML private TextField localDirField;
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField loginField;

    @FXML private PasswordField passwordField;

    @FXML
    public void connect() {
        boolean loginFilled = validate(loginField);
        boolean passwordFilled = validate(passwordField);
        boolean hostFilled = validate(hostField);
        boolean portFilled = validate(portField);

        if ( loginFilled && passwordFilled && hostFilled && portFilled ){
            String address = hostField.getText();
            String login = loginField.getText();
            String password = passwordField.getText();
            int port = Integer.parseInt(portField.getText());

            try {
                new ConnectionManager(address, port);
            } catch (IOException e) {
                createWarningDialog("Connection error", "Cannot connect to the remote host");
            }
        }
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
