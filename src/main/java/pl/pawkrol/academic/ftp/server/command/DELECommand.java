package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.sql.SQLException;

/**
 * Created by pawkrol on 5/29/16.
 */
public class DELECommand extends Command{

    public DELECommand(Session session) {
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        try {
            if (!session.getFileManager().removeFile(params[0], session.getUser())){
                return new Response(550, "Cannot delete \"" + params[0] + "\"");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(550, "Cannot delete \"" + params[0] + "\"");
        }

        return new Response(250, params[0] + "\" deleted");
    }
}
