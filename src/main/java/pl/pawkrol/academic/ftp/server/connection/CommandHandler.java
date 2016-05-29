package pl.pawkrol.academic.ftp.server.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.command.CommandDispatcher;
import pl.pawkrol.academic.ftp.server.session.Session;
import pl.pawkrol.academic.ftp.server.session.SessionManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Pawel on 2016-03-19.
 */
public class CommandHandler implements Runnable{

    private static final Logger log = LogManager.getLogger("logger");
    private final Socket socket;
    private final Session session;
    private final CommandDispatcher commandDispatcher;
    private final ConnectionManager connectionManager;

    private OutputStream clientOutputStream;

    private int timeout;

    public CommandHandler(ConnectionManager connectionManager, SessionManager sessionManager,
                          Socket socket) {
        this.session = sessionManager.createSession(this);
        this.socket = socket;
        this.timeout = 60; //seconds
        this.commandDispatcher = new CommandDispatcher(session);
        this.connectionManager = connectionManager;
    }

    @Override
    public void run(){
        try {
            socket.setSoTimeout(timeout * 1000);

            log.log(Level.INFO, "Connection from: "
                    + socket.getInetAddress().getCanonicalHostName());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            clientOutputStream = socket.getOutputStream();
            sendResponse(new Response(220, "Server ready."));

            String command;
            while (session.isAlive() && (command = reader.readLine()) != null
                    && (!Thread.currentThread().isInterrupted())){
                sendResponse(commandDispatcher.dispatch(command));
            }

            handleClose();

        } catch (SocketTimeoutException e){
            onTimeout();
        } catch (IOException e) {
            e.printStackTrace();
            handleClose();
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kill() throws IOException { //try it all
        socket.close();
        close();
        Thread.currentThread().interrupt();
    }

    public void sendResponse(Response response) throws IOException {
        clientOutputStream.write(response.toString().getBytes());
        clientOutputStream.flush();
    }

    private void handleClose() {
        if (session.getUser() != null) {
            log.log(Level.INFO, "End of connection from: " + session.getUser().getUsername()
                    + "@" + socket.getInetAddress().getCanonicalHostName());
        } else {
            log.log(Level.INFO, "End of connection from: "
                    + socket.getInetAddress().getCanonicalHostName());
        }
        session.close();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onTimeout() {
        log.log(Level.WARN, "Timeout terminating...");

        try {
            sendResponse(new Response(426, "Timeout termination after " + timeout + " seconds."));
        } catch (IOException e) {
            e.printStackTrace();
        }

        handleClose();
    }
}
