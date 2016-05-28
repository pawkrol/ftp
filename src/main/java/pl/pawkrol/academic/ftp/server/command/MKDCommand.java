package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.Main;
import pl.pawkrol.academic.ftp.server.filesystem.FTPFile;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by pawkrol on 5/28/16.
 */
public class MKDCommand extends Command{

    public MKDCommand(Session session) {
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        String filename = params[0] + (params[0].endsWith("/") ? "" : "/");
        try {
            FTPFile ftpFile = session.getFileManager().getFTPFile(filename, session.getUser());
            if (ftpFile != null && ftpFile.isDirectory()){
                return new Response(550, "Directory \"" + params[0] + "\" already exists");
            }

            String filepath = session.getFileManager()
                                    .constructFilePath(filename, session.getUser());
            File file = new File(filepath);
            if (!file.mkdir()){
                return new Response(550, "Cannot create \"" + params[0] + "\" directory");
            }

            session.getFileManager().putFileRecord(constructDBFilename(filename), true, true,
                                                    session.getUser());
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(550, "Cannot create \"" + params[0] + "\" directory");
        }

        return new Response(250, "Directory \"" + params[0] + "\" created");
    }

    private String constructDBFilename(String filename){
        String currentDir = session.getFileManager().getCurrentDir();
        return currentDir + filename;
    }

}
