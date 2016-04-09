package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-04-09.
 */
public class USERCommand extends Command {

    USERCommand(Session session){
        this.session = session;
        this.paramsNumber = 1;
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        if (session.state == Session.State.AUTHENTICATED){
            return new Response(501, "Already logged in.");
        }

        session.getUser().setUsername(params[0]);
        session.state = Session.State.AUTHENTICATING;

        return new Response(331, "Please identify yourself in a password.");
    }
}
