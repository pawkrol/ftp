package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.PassiveDataHandler;
import pl.pawkrol.academic.ftp.server.connection.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.net.ServerSocket;

/**
 * Created by Pawel on 2016-04-10.
 */
public class PASVCommand extends Command {

    public PASVCommand(Session session) {
        super(session, 0);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        session.mode = Session.Mode.PASSIVE;
        PassiveDataHandler passiveDataHandler = session.requestPassiveDataHandler();

        if (passiveDataHandler == null){
            return new Response(425, "Cannot open data connection.");
        }

        return new Response(227, createPassiveResponse(passiveDataHandler.getServerSocket()));
    }

    private String createPassiveResponse(ServerSocket serverSocket){
        byte[] address = serverSocket.getInetAddress().getAddress();
        int port = serverSocket.getLocalPort();

        return String.format("%d,%d,%d,%d,%d,%d", address[0], address[1], address[2], address[3],
                                                    port / 256, port % 256);
    }
}
