package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.connection.DataProcessor;
import pl.pawkrol.academic.ftp.server.db.NotDirectoryException;
import pl.pawkrol.academic.ftp.server.filesystem.FTPFile;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by pawkrol on 5/24/16.
 */
public class LISTCommand extends Command{

    public LISTCommand(Session session){
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null){
            return response;
        }

        if (session.getDataHandler() == null){
            return new Response(425, "No connection established");
        }

        try {
            List<FTPFile> files =
                    session.getFileManager().getFilesFromDirectory(params[0], session.getUser());

            session.getDataHandler().setDataProcessor(new DataProcessor() {
                Socket socket;

                @Override
                public void execute(Socket socket) {
                    this.socket = socket;

                    try {
                        for (FTPFile file : files) {
                            socket.getOutputStream().write(
                                    (formatFileEntry(file) + "\n").getBytes()
                            );
                        }
                        socket.getOutputStream().flush();

                        session.getCommandHandler().sendResponse(new Response(226, "Transfer complete"));
                        session.getDataHandler().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void disconnect() {
                    try {
                        socket.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    session.getDataHandler().setDataProcessor(null);
                }
            });


        } catch (FileNotFoundException e) {
            return new Response(451, "File not found");
        } catch (NotDirectoryException e) {
            return new Response(550, "File is not a directory");
        }

        return new Response(150, "Opening ASCII mode data connection");
    }

    private String formatFileEntry(FTPFile file){
        return String.format("%s", file.getPath() + (file.isDirectory() ? "/" : ""));
    }
}
