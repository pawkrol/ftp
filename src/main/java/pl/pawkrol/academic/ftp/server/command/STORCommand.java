package pl.pawkrol.academic.ftp.server.command;

import org.apache.commons.io.FilenameUtils;
import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.Main;
import pl.pawkrol.academic.ftp.server.connection.DataProcessor;
import pl.pawkrol.academic.ftp.server.filesystem.FTPFile;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by pawkrol on 5/28/16.
 */
public class STORCommand extends Command {

    public STORCommand(Session session) {
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        if (session.getDataHandler() == null) {
            return new Response(425, "No connection established");
        }

        try {
            String filename = checkFilename(params[0]);
            String filePath = constructFilePath(filename);
            session.getFileManager().putFileRecord(
                    constructDBFilename(filename), true, true, session.getUser()
            );
            FileOutputStream fos = new FileOutputStream(filePath);

            session.getDataHandler().setDataProcessor(new DataProcessor() {
                Socket socket;

                @Override
                public void execute(Socket socket) {
                    this.socket = socket;
                    try {
                        InputStream is = socket.getInputStream();

                        int c;
                        byte[] buff = new byte[1024];
                        while ((c = is.read(buff)) > 0){
                            fos.write(buff, 0, c);
                        }

                        session.getCommandHandler().sendResponse(
                                new Response(226, "File \"" + params[0] + "\" successfully stored")
                        );
                        session.getDataHandler().close();
                    } catch (IOException e) {
                      //  e.printStackTrace();
                    }
                }

                @Override
                public void disconnect() {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    session.getDataHandler().setDataProcessor(null);
                }
            });
        }catch (FileNotFoundException e){
            return new Response(452, "Cannot create file \"" + params[0] + "\"");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(451, "Cannot store file \"" + params[0] + "\"");
        }

        return new Response(150, "Opening ASCII mode data connection");
    }

    private String constructFilePath(String filename){
        String currentDir = session.getFileManager().getCurrentDir();
        String fileDir = Main.rootPath.toString() + "/"
                + session.getUser().getUsername() + "/" + currentDir;
        if (currentDir.endsWith("/")){
            return fileDir + filename;
        } else {
            return fileDir + "/" + filename;
        }
    }

    private String constructDBFilename(String filename){
        String currentDir = session.getFileManager().getCurrentDir();
        return currentDir + filename;
    }

    private String checkFilename(String filename){
        FTPFile ftpFile = session.getFileManager().getFTPFile(
                constructDBFilename(filename), session.getUser()
        );

        if (ftpFile == null) {
            return filename;
        } else {
            String ext = FilenameUtils.getExtension(filename);
            String name = FilenameUtils.getBaseName(filename);
            filename = name + "_copy" + (ext.isEmpty() ? "" : ("." + ext));

            return checkFilename(filename);
        }
    }
}
