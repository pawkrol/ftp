package pl.pawkrol.academic.ftp.client.filesystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.client.connection.DataConnector;
import pl.pawkrol.academic.ftp.client.message.transfer.LISTMessage;
import pl.pawkrol.academic.ftp.client.message.plain.PWDMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by pawkrol on 5/26/16.
 */
public class RemoteFilesystem {

    private String workingDirectory = "?";
    private CommandHandler commandHandler;
    private ConnectionManager connectionManager;
    private ObservableList<String> files = FXCollections.observableArrayList();

    public RemoteFilesystem(ConnectionManager connectionManager){
        this.commandHandler = connectionManager.getCommandHandler();
        this.connectionManager = connectionManager;
    }

    public void updateWorkingDirectory(){
        commandHandler.sendMessage(new PWDMessage(), response ->
            workingDirectory = response.getMessage().replace("\"", "")
        );
    }

    public void updateRemoteDirList(){
        connectionManager.getDataHandler().setConnector(new DataConnector() {
            Socket socket;
            BufferedReader reader;

            @Override
            public void init(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void connect() {
                try {
                    reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream())
                    );

                    files = FXCollections.observableArrayList();
                    String line;
                    while ((line = reader.readLine()) != null){
                        files.add(line);
                    }

                } catch (IOException e) {
                    //may be interrupted, ignore
                    //e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void close() {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        commandHandler.sendMessage(new LISTMessage(workingDirectory), null);
    }

    public ObservableList<String> getFiles(){
        return files;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }
}
