package pl.pawkrol.academic.ftp.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by pawkrol on 3/7/16.
 */
public class ConnectionManager {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private int port;

    public ConnectionManager(int port){
        this.port = port;

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        new Thread( () -> {
            while (true) {

                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void stop() throws IOException {
        clientSocket.close();
    }

    public int getPort(){
        return port;
    }

}
