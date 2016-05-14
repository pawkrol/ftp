package pl.pawkrol.academic.ftp.client.connection;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by pawkrol on 4/25/16.
 */
public class ConnectionManager {

    private final Socket socket;

    public ConnectionManager(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
    }

}
