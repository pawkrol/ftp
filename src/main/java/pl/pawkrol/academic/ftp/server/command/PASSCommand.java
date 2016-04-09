package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-04-09.
 */
public class PASSCommand extends Command {

    PASSCommand(Session session){
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

        session.getUser().setPassword(params[0]);
        if (session.authenticate()){
            return new Response(230, "Logged in.");
        } else {
            return new Response(430, "Invalid username or password.");
        }
    }
}
