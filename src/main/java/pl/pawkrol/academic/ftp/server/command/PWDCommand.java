package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

/**
 * Created by pawkrol on 4/24/16.
 */
public class PWDCommand extends Command{

    public PWDCommand(Session session){
        super(session, 0);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        String workingDir = session.getFileManager().getCurrentDir();
//        if (workingDir.equals("~/")){
//            workingDir = Main.rootPath.toString() + "/" + session.getUser().getUsername() + "/";
//        }

        return new Response(257, "\"" + workingDir + "\"");
    }
}
