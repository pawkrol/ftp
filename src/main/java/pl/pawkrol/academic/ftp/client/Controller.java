package pl.pawkrol.academic.ftp.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.client.filesystem.LocalFilesystem;
import pl.pawkrol.academic.ftp.client.filesystem.RemoteFilesystem;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.message.MessageResponsePair;
import pl.pawkrol.academic.ftp.client.message.plain.CWDMessage;
import pl.pawkrol.academic.ftp.client.session.User;
import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.common.utils.ListViewAppender;
import pl.pawkrol.academic.ftp.common.utils.LogWrapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by pawkrol on 4/24/16.
 */
public class Controller implements Initializable{

    @FXML private TreeView<File> localFilesView;
    @FXML private ListView<String> remoteFilesView;
    @FXML private ListView<LogWrapper> rawResponseView;
    @FXML private ListView<String> transferView;

    @FXML private TextField remoteDirField;
    @FXML private TextField localDirField;
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private TextField loginField;

    @FXML private PasswordField passwordField;

    @FXML private Button connectButton;

    private boolean connected = false;
    private ConnectionManager connectionManager;
    private LocalFilesystem localFilesystem;
    private RemoteFilesystem remoteFilesystem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localFilesystem = new LocalFilesystem(Paths.get("/").toAbsolutePath());
        ListViewAppender.setListView(rawResponseView);
        setLocalFileListAndPath();
        localFilesView.setCellFactory(param -> new TreeCellFactory());
    }

    @FXML
    public void connectButtonAction(){
        if (!connected){
            try {
                connect();

                if (connectionManager != null){
                    connectionManager.getCommandHandler()
                                        .registerMessageResponseListener(this::onAnyResponse);
                    connectionManager.getCommandHandler()
                                        .registerMessageResponseListener(
                                                new TransferWatcher(transferView)::watch
                                        );
                    remoteFilesystem = new RemoteFilesystem(connectionManager);
                    connectionManager.open();
                }

                connected = true;
                connectButton.setText("Disconnect");
                remoteDirField.setDisable(false);
                remoteFilesView.setDisable(false);

            } catch (IOException e) {
                createWarningDialog("Connection error", "Cannot setConnector to the remote host");
            } catch (EmptyFiledException e){
                //Do nothing
            }
        } else {
            disconnect();
        }
    }

    @FXML
    public void onEnterLocalDirField(){
        localFilesystem.setWorkingDirectory(localDirField.getText());
        setLocalFileListAndPath();
    }

    @FXML
    public void onEnterRemoteDirField(){
        connectionManager.getCommandHandler()
                .sendMessage(new CWDMessage(remoteDirField.getText()), null);
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public synchronized void onAnyResponse(MessageResponsePair messageResponsePair){
        Response response = messageResponsePair.getResponse();
        Message message = messageResponsePair.getMessage();

        if (response.getCode() == 426){
            disconnect();
            return;
        }

        switch (message.getCommand()){
            case "PASS":
                if (response.getCode() == 230){
                    remoteFilesystem.updateWorkingDirectory();
                } else if (response.getCode() != 501) {
                    disconnect();
                }
                break;
            case "PWD":
                onInitRemoteDir();
                remoteFilesystem.updateRemoteDirList();
                break;
            case "LIST":
                if (response.getCode() == 226) {
                    setRemoteFileList();
                }
                break;
            case "CWD":
                if (response.getCode() == 250){
                    remoteFilesystem.updateWorkingDirectory();
                } else if (response.getCode() == 550){
                    Platform.runLater(() ->
                            remoteDirField.setText(remoteFilesystem.getWorkingDirectory())
                    );
                }
                break;
        }
    }

    private void connect() throws IOException, EmptyFiledException {
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
        } else {
            throw new EmptyFiledException("Not all fields are filled");
        }

    }

    private void onInitRemoteDir(){
        Platform.runLater(() ->
            remoteDirField.setText(
                    remoteFilesystem.getWorkingDirectory()
            )
        );
    }

    private void setLocalFileListAndPath(){
        localDirField.setText(localFilesystem.getWorkingDirectory().toString());
        TreeItem<File> treeItem = new TreeListItem(new File(localDirField.getText()));
        treeItem.setExpanded(true);
        localFilesView.setRoot(treeItem);
    }

    private void setRemoteFileList(){
        Platform.runLater(() ->
                remoteFilesView.setItems(remoteFilesystem.getFiles())
        );
    }

    private void disconnect(){
        Platform.runLater(() -> {
            connectionManager.close();

            connected = false;
            connectButton.setText("Connect");
            remoteDirField.setDisable(true);
            remoteFilesView.setDisable(true);
        });
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

    private final class TreeCellFactory extends TreeCell<File>{
        @Override
        protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
            } else {
                if (getItem().getParentFile() == null){
                    setText("/");
                } else {
                    setText(getItem() == null ? "" : getItem().getName());
                }
            }
        }
    }
}
