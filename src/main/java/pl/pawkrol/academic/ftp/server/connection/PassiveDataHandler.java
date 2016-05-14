package pl.pawkrol.academic.ftp.server.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Pawel on 2016-04-10.
 */
public class PassiveDataHandler extends DataHandler {

    private static final Logger log = LogManager.getLogger("logger");

    private ServerSocket serverSocket;
    private boolean running = true;

    public PassiveDataHandler(Session session) {
        super(session);
        createServerSocket();
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket socket = serverSocket.accept();
                log.log(Level.INFO, "Passive connection with: "
                                + socket.getInetAddress().getCanonicalHostName());

                if (dataProcessor == null){
                    session.getCommandHandler().sendResponse(new Response(426, "Connection broken"));
                    close();
                    break;
                }

                dataProcessor.execute(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void close(){
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
       //     e.printStackTrace();
        }
    }

    private void createServerSocket(){
        try {
            serverSocket = new ServerSocket(0, 0, InetAddress.getLoopbackAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
