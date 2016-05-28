package pl.pawkrol.academic.ftp.client.session;

import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.client.connection.DataHandler;

/**
 * Created by pawkrol on 4/25/16.
 */
public class Session {

    public enum State {
        NOT_AUTHENTICATED, AUTHENTICATED
    }
    public State state;

    public enum Mode{
        UNKNOWN, PASSIVE, ACTIVE
    }
    public Mode mode;

    private final ConnectionManager connectionManager;
    private final User user;

    private CommandHandler commandHandler;
    private DataHandler dataHandler;

    public Session(ConnectionManager connectionManager, User user) {
        this.connectionManager = connectionManager;
        this.user = user;

        this.state = State.NOT_AUTHENTICATED;
        this.mode = Mode.PASSIVE;
    }

    public void close(){
        if (commandHandler != null){
            commandHandler.close();
        }
        if (dataHandler != null){
            dataHandler.close();
        }
    }

    public void authenticate(){
        Authenticator authenticator = new Authenticator(connectionManager.getCommandHandler());
        authenticator.authenticate(this);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        if (dataHandler != null){
            dataHandler.close();
        }
        this.dataHandler = dataHandler;
    }

    public User getUser() {
        return user;
    }
}
