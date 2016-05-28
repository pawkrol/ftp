package pl.pawkrol.academic.ftp.client.connection;

import java.net.Socket;

/**
 * Created by pawkrol on 5/26/16.
 */
public interface DataConnector {

    void init(Socket socket);
    void connect();
    void close();
}
