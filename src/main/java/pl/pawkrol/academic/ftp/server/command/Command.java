package pl.pawkrol.academic.ftp.server.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by Pawel on 2016-03-19.
 */
public abstract class Command {

    protected static final Logger log = LogManager.getLogger("logger");

    protected int paramsNumber;
    protected Session session;

    Command(Session session, int paramsNumber){
        this.session = session;
        this.paramsNumber = paramsNumber;
    }

    public Response execute(String[] params){
        if (params.length < paramsNumber || params.length > paramsNumber){
            return new Response(501, "Wrong number of arguments, expected: "
                                        + paramsNumber + " have " + params.length);
        }

        return null;
    }
}
