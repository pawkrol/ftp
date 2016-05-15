package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-04-10.
 */
public class QUITCommand extends Command {

    QUITCommand(Session session){
        super(session, 0);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        session.kill();

        return new Response(221, "Bye.");
    }
}
