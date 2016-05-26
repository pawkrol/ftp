package pl.pawkrol.academic.ftp.server.connection;

import java.net.Socket;

/**
 * Created by pawkrol on 4/23/16.
 */
public interface DataProcessor {

    void execute(Socket socket);

    void disconnect();

}
