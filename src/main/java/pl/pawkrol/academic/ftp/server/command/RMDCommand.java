package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by pawkrol on 5/29/16.
 */
public class RMDCommand extends Command{

    public RMDCommand(Session session) {
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        try {
            session.getFileManager().removeDirectory(params[0], session.getUser());
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(550, "Cannot delete \"" + params[0] + "\"");
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(550, "Cannot delete \"" + params[0] + "\"");
        }

        return new Response(250, "\"" + params[0] + "\" deleted");
    }

}
