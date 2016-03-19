package pl.pawkrol.academic.ftp.server.connection;

import pl.pawkrol.academic.ftp.server.command.CommandHandler;
import pl.pawkrol.academic.ftp.server.session.SessionManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pawkrol on 3/7/16.
 */
public class ConnectionManager {

    private final int port;
    private final int pool;
    private final SessionManager sessionManager;

    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public ConnectionManager(SessionManager sessionManager, int port, int pool){
        this.executorService = Executors.newFixedThreadPool(pool);
        this.sessionManager = sessionManager;
        this.port = port;
        this.pool = pool;
    }

    public void run(){
        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (true) {
                    try {
                        executorService.execute(new CommandHandler(serverSocket.accept()));
                    } catch (IOException e) {
                        //  will be thrown on socket close
                    }
                    if (serverSocket.isClosed()) {
                        break;
                    }
                }
            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if (serverSocket != null
                && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public int getPort(){
        return port;
    }

}
