package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-03-19.
 */
public abstract class Command {

    protected int paramsNumber;
    protected Session session;

    public Response execute(String[] params){
        if (params.length < paramsNumber || params.length > paramsNumber){
            return new Response(501, "Wrong number of arguments, expected: "
                                        + paramsNumber + " has " + params.length);
        }

        return null;
    }
}
