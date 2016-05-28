package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.DataProcessor;
import pl.pawkrol.academic.ftp.common.Response;
import pl.pawkrol.academic.ftp.server.filesystem.IsDirectoryException;
import pl.pawkrol.academic.ftp.server.filesystem.PermissionDeniedException;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.*;
import java.net.Socket;

/**
 * Created by pawkrol on 4/23/16.
 */
public class RETRCommand extends Command{

    public RETRCommand(Session session){
        super(session, 1);
    }

    @Override
    public Response execute(String[] params) {
        Response response = super.execute(params);
        if (response != null) {
            return response;
        }

        if (session.getDataHandler() == null){
            return new Response(425, "No connection established");
        }

        try {
            InputStream fis = session.getFileManager().getFileInputStream(params[0], session.getUser());

            session.getDataHandler().setDataProcessor(new DataProcessor() {
                Socket socket;

                @Override
                public void execute(Socket socket) {
                    this.socket = socket;
                    try {

                        int c;
                        byte[] buff = new byte[1024];
                        while ((c = fis.read(buff)) > 0){
                            socket.getOutputStream().write(buff, 0, c);
                        }

                        session.getCommandHandler().sendResponse(
                                new Response(226, "File \"" + params[0] + "\" send successful")
                        );
                        session.getDataHandler().close();
                    } catch (IOException e) {
                        e.printStackTrace();
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
        } catch (FileNotFoundException e) {
            return new Response(451, "File \"" + params[0] + "\" not found");
        } catch (PermissionDeniedException e) {
            return new Response(550, "No permissions to access file \"" + params[0] + "\"");
        } catch (IsDirectoryException e){
            return new Response(451, "File \"" + params[0] + "\" is directory");
        }

        return new Response(150, "I see that file");
    }
}
