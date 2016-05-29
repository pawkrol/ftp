package pl.pawkrol.academic.ftp.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.control.TextField;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.client.EmptyFiledException;
import pl.pawkrol.academic.ftp.common.User;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.common.utils.ListViewAppender;
import pl.pawkrol.academic.ftp.common.utils.LogWrapper;
import pl.pawkrol.academic.ftp.server.filesystem.UserCreator;

import javax.swing.event.DocumentEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private ConnectionManager connectionManager;
    private static final Logger log = LogManager.getLogger("logger");

    @FXML private Button button;

    @FXML private TextField loginUserField;
    @FXML private TextField passwordUserField;

    @FXML private ListView<LogWrapper> logger;

    @FXML private Label portLabel;

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

    @FXML
    public void onAddUser(){
        boolean isLogin = validate(loginUserField);
        boolean isPassword = validate(passwordUserField);

        if (isLogin && isPassword){
            String login = loginUserField.getText();
            String password = passwordUserField.getText();

            User user = new User(login, password);

            try {
                new UserCreator(connectionManager.getDbConnector())
                        .create(user);
                loginUserField.clear();
                passwordUserField.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("Duplicates")
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
}