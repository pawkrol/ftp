package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.filesystem.FileDoesNotExistsException;
import pl.pawkrol.academic.ftp.server.filesystem.NotDirectoryException;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by pawkrol on 5/27/16.
 */
public class CWDCommand extends Command{

    public CWDCommand(Session session){
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        try {
            session.getFileManager().changeDir(params[0], session.getUser());
            return new Response(250, "CWD command successful");
        } catch (NotDirectoryException e) {
            return new Response(550, params[0] + " is not directory");
        } catch (FileDoesNotExistsException e) {
            return new Response(550, params[0] + " does not exists");
        }
    }
}
