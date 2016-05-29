package pl.pawkrol.academic.ftp.client.filesystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pawkrol.academic.ftp.client.TransferWatcher;
import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.client.connection.DataConnector;
import pl.pawkrol.academic.ftp.client.message.plain.*;
import pl.pawkrol.academic.ftp.client.message.transfer.RETRMessage;
import pl.pawkrol.academic.ftp.client.message.transfer.LISTMessage;
import pl.pawkrol.academic.ftp.client.message.transfer.STORMessage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Collections;

/**
 * Created by pawkrol on 5/26/16.
 */
public class RemoteFilesystem {

    private String workingDirectory = "?";
    private CommandHandler commandHandler;
    private ConnectionManager connectionManager;
    private ObservableList<String> files = FXCollections.observableArrayList();
    private TransferWatcher transferWatcher;

    public RemoteFilesystem(ConnectionManager connectionManager, TransferWatcher transferWatcher){
        this.commandHandler = connectionManager.getCommandHandler();
        this.connectionManager = connectionManager;
        this.transferWatcher = transferWatcher;
    }

    public void updateWorkingDirectory(){
        commandHandler.sendMessage(new PWDMessage(), response ->
            workingDirectory = response.getMessage().replace("\"", "")
        );
    }

    public void changeWorkingDirecory(String path){
        commandHandler.sendMessage(new CWDMessage(path), null);
    }

    public void addDirectory(String name){
        commandHandler.sendMessage(new MKDMessage(name), null);
    }

    public void removeDirectory(String name){
        if (name.endsWith("/")){
            commandHandler.sendMessage(new RMDMessage(name), null);
        } else {
            commandHandler.sendMessage(new DELEMessage(name), null);
        }
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
                        if (reader != null)
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

    public void uploadFile(String filepath){
        String filename = Paths.get(filepath).getFileName().toString();
        connectionManager.getDataHandler().setConnector(new DataConnector() {
            Socket socket;

            long time;
            int bytes = 0;

            @Override
            public void init(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void connect() {
                try {
                    FileInputStream fis = new FileInputStream(new File(filepath));

                    int c;
                    byte[] buff = new byte[1024];
                    final long stime = System.nanoTime();
                    while ((c = fis.read(buff)) > 0){
                        socket.getOutputStream().write(buff, 0, c);
                        bytes += c;
                    }
                    final long etime = System.nanoTime();

                    time = (etime - stime) / 1000000;

                    transferWatcher.addEntry(TransferWatcher.Type.UPLOAD, filename,
                            time, bytes);
                } catch (IOException e) {
                   // e.printStackTrace();
                } finally {
                    close();
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
        commandHandler.sendMessage(new STORMessage(filename), null);
    }

    public void downloadFile(String remotePath, String localPath){
        connectionManager.getDataHandler().setConnector(new DataConnector() {
            Socket socket;

            long time;
            int bytes = 0;
            long stime;

            @Override
            public void init(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void connect() {
                try {
                    FileOutputStream fos = new FileOutputStream(localPath);
                    InputStream is = socket.getInputStream();

                    int c;
                    byte[] buff = new byte[1024];
                    stime = System.nanoTime();
                    while ((c = is.read(buff)) > 0){
                        fos.write(buff, 0, c);
                        bytes += c;
                    }
                } catch (IOException e) {
                   // e.printStackTrace();
                } finally {
                    close();

                    long etime = System.nanoTime();
                    time = (etime - stime) / 1000000;
                    transferWatcher.addEntry(TransferWatcher.Type.DOWNLOAD, remotePath,
                            time, bytes);
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
        commandHandler.sendMessage(new RETRMessage(remotePath), null);
    }

    public ObservableList<String> getFileList(){
        Collections.sort(files, Collator.getInstance());
        if (!workingDirectory.equals("/")){
            files.add(0, "..");
        }
        return files;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }
}
