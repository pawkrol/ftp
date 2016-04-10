package pl.pawkrol.academic.ftp.server.session;

import pl.pawkrol.academic.ftp.server.connection.CommandHandler;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.server.connection.DataHandler;
import pl.pawkrol.academic.ftp.server.connection.PassiveDataHandler;
import pl.pawkrol.academic.ftp.server.db.User;

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

    private DataHandler dataHandler;

    private int id;
    private User user;

    public Session(SessionManager sessionManager, CommandHandler commandHandler,
                   ConnectionManager connectionManager,
                   Authenticator authenticator, int id) {

        this.sessionManager = sessionManager;
        this.commandHandler = commandHandler;
        this.connectionManager = connectionManager;
        this.authenticator = authenticator;
        this.id = id;

        this.state = State.NOT_AUTHENTICATED;
        this.mode = Mode.UNKNOWN;
        this.user = new User();
    }

    public boolean authenticate(){
        if (authenticator.authenticate(user)){

            state = State.AUTHENTICATED;
            return true;
        }

        return false;
    }

    public void close(){
        if (commandHandler != null){
            commandHandler.close();
        }

        sessionManager.removeSession(this);
    }

    public PassiveDataHandler requestPassiveDataHandler(){
        if (dataHandler != null) {
            dataHandler.close();
        }

        dataHandler = connectionManager.obtainDataHandler(this);
        dataHandler.run();

        return (PassiveDataHandler) dataHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
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
}
