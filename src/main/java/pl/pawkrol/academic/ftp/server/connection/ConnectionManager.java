package pl.pawkrol.academic.ftp.server.connection;

import pl.pawkrol.academic.ftp.server.db.DBConnector;
import pl.pawkrol.academic.ftp.server.session.Session;
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
    private final DBConnector dbConnector;

    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public ConnectionManager(int port, int pool, DBConnector dbConnector){
        this.sessionManager = new SessionManager(this, dbConnector);
        this.dbConnector = dbConnector;
        this.port = port;
        this.pool = pool;
    }

    public void run(){
        executorService = Executors.newFixedThreadPool(pool);

        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                while (true) {
                    try {
                        executorService.execute(
                                new CommandHandler(this, sessionManager, serverSocket.accept())
                        );
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
        sessionManager.closeAllSessions();

        if (serverSocket != null
                && !serverSocket.isClosed()) {
            serverSocket.close();
        }

        if (executorService != null) {
            executorService.shutdown();
            executorService.shutdownNow();
        }
    }

    public DataHandler obtainDataHandler(Session session){
        switch (session.mode) {
            case UNKNOWN:
                return null;

            case PASSIVE:
                PassiveDataHandler passiveDataHandler = new PassiveDataHandler(session);
                new Thread(passiveDataHandler).start();
                return passiveDataHandler;

            case ACTIVE:
                return null;
        }

        return null;
    }

    public DBConnector getDbConnector() {
        return dbConnector;
    }

    public int getPort(){
        return port;
    }

    public int getPool() {
        return pool;
    }
}
