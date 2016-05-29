package pl.pawkrol.academic.ftp.server.session;

import pl.pawkrol.academic.ftp.server.connection.*;
import pl.pawkrol.academic.ftp.common.User;
import pl.pawkrol.academic.ftp.server.filesystem.FileManager;

/**
 * Created by Pawel on 2016-03-19.
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

    private final Authenticator authenticator;
    private final SessionManager sessionManager;
    private final ConnectionManager connectionManager;
    private final CommandHandler commandHandler;
    private final FileManager fileManager;

    private DataHandler dataHandler;

    private int id;
    private User user;
    private boolean alive;

    public Session(SessionManager sessionManager, CommandHandler commandHandler,
                   ConnectionManager connectionManager,
                   Authenticator authenticator, int id,
                   FileManager fileManager) {

        this.sessionManager = sessionManager;
        this.commandHandler = commandHandler;
        this.connectionManager = connectionManager;
        this.fileManager = fileManager;
        this.authenticator = authenticator;
        this.id = id;

        this.state = State.NOT_AUTHENTICATED;
        this.mode = Mode.UNKNOWN;
        this.user = new User();
        this.alive = true;
    }

    public boolean authenticate(){
        if (authenticator.authenticate(user)){

            state = State.AUTHENTICATED;
            return true;
        }

        return false;
    }

    public void close(){
        closeAllHandlers();
        sessionManager.removeSession(this);
    }

    public void closeAllHandlers(){
        if (commandHandler != null){
            commandHandler.close();
        }

        if (dataHandler != null){
            dataHandler.close();
        }
    }

    public PassiveDataHandler requestPassiveDataHandler(){
        if (dataHandler != null) {
            dataHandler.close();
        }

        dataHandler = connectionManager.obtainDataHandler(this);

        return (PassiveDataHandler) dataHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public State getState() {
        return state;
    }

    public void kill() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}
