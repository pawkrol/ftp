package pl.pawkrol.academic.ftp.server.connection;

import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by Pawel on 2016-04-10.
 */
public class PassiveDataHandler extends DataHandler {

    private final InetAddress address;
    private ServerSocket serverSocket;

    public PassiveDataHandler(Session session, InetAddress address) {
        super(session);
        this.address = address;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(0, 0, InetAddress.getLocalHost());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
