package pl.pawkrol.academic.ftp.client.session;

import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.connection.ConnectionManager;

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

    public Session(ConnectionManager connectionManager, User user) {
        this.connectionManager = connectionManager;
        this.user = user;

        this.state = State.NOT_AUTHENTICATED;
        this.mode = Mode.UNKNOWN;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void close(){
        if (commandHandler != null){
            commandHandler.close();
        }
    }

    public boolean authenticate(){
       if (new Authenticator(connectionManager.getMessageProcessor()).authenticate(user)){
           state = State.AUTHENTICATED;
           return true;
       } else {
           return false;
       }
    }


}
