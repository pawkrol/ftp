package pl.pawkrol.academic.ftp.server;

import org.junit.Assert;
import org.junit.Test;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.server.session.SessionManager;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Pawel on 2016-03-20.
 */
public class ConnectionTest {

    @Test
    public void connectTest() throws IOException {
        ConnectionManager connectionManager = new ConnectionManager(2121, 4);
        connectionManager.run();

        Socket client = new Socket("127.0.0.1", 2121);

        Scanner scanner = new Scanner(client.getInputStream());
        String response = scanner.nextLine();
        Assert.assertEquals(response, "220 Server ready.");

        connectionManager.close();
    }

}
