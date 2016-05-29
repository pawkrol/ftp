package pl.pawkrol.academic.ftp.client.session;

import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.message.AUTHMessage;
import pl.pawkrol.academic.ftp.common.User;

/**
 * Created by pawkrol on 5/15/16.
 */
public class Authenticator {

    enum Status{
        NOT_AUTHENTICATED,
        AUTHENTICATED
    }
    public Status status;

    private final CommandHandler commandHandler;

    private User user;
    private Session session;

    public Authenticator(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.status = Status.NOT_AUTHENTICATED;
    }

    public void authenticate(Session session){
        this.user = session.getUser();
        this.session = session;

        if (status.equals(Status.NOT_AUTHENTICATED)) {
            commandHandler.sendMessage(new AUTHMessage(user.getUsername(), user.getPassword()), response -> {
                if (response.getCode() == 230){
                    status = Status.AUTHENTICATED;
                    session.state = Session.State.AUTHENTICATED;
                }
            });
        }

    }

}
