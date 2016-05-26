package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-04-09.
 */
public class USERCommand extends Command {

    USERCommand(Session session){
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        if (session.state == Session.State.AUTHENTICATED){
            return new Response(666, "Already logged in.");
        }

        session.getUser().setUsername(params[0]);

        return new Response(331, "Please identify yourself in a password.");
    }
}
