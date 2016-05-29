package pl.pawkrol.academic.ftp.client.connection;

import pl.pawkrol.academic.ftp.client.RawResponseDispatcher;
import pl.pawkrol.academic.ftp.client.message.plain.QUITMessage;
import pl.pawkrol.academic.ftp.client.session.Session;
import pl.pawkrol.academic.ftp.common.User;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pawkrol on 4/25/16.
 */
public class ConnectionManager {

    private final int port;
    private final String address;
    private final Socket socket;
    private final Session session;
    private final CommandHandler commandHandler;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ConnectionManager(String address, int port, User user) throws IOException {
        this.port = port;
        this.address = address;
        this.socket = new Socket(address, port);
        this.session = new Session(this, user);
        this.commandHandler = new CommandHandler(socket, session, executorService, this);

        commandHandler.registerMessageResponseListener(new RawResponseDispatcher()::dispatch);
    }

    public void open(){
        commandHandler.init();
    }

    //Graceful close
    public void close(){
        if (!socket.isClosed() && commandHandler != null) {
            commandHandler.sendMessage(new QUITMessage(), response -> kill());
        } else {
            kill();
        }
    }

    public void kill(){
        try {
            socket.close();
            session.close();

            executorService.shutdown();
            executorService.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataHandler getDataHandler(){
        Session.Mode mode = session.mode;
        if (session.getDataHandler() != null){
            return session.getDataHandler();
        }

        if (mode.equals(Session.Mode.PASSIVE)){
            session.setDataHandler(new PassiveDataHandler(commandHandler));
            return session.getDataHandler();
        } else if (mode.equals(Session.Mode.ACTIVE)) {
            return null; //no active yet
        }

        return null;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }
}
