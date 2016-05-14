package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.DataProcessor;
import pl.pawkrol.academic.ftp.server.connection.Response;
import pl.pawkrol.academic.ftp.server.filesystem.PermissionDeniedException;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            FileReader fileReader
                    = session.getFileManager().getFileReader(params[0], session.getUser());

            session.getDataHandler().setDataProcessor(new DataProcessor() {
                Socket socket;

                @Override
                public void execute(Socket socket) {
                    this.socket = socket;
                    try {

                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        String line;
                        while ((line = bufferedReader.readLine()) != null){
                            socket.getOutputStream().write(line.getBytes());
                        }

                        socket.getOutputStream().flush();

                        session.getCommandHandler().sendResponse(new Response(226, "File send successful"));
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
            return new Response(451, "File not found");
        } catch (PermissionDeniedException e) {
            return new Response(550, "No permissions to access file");
        }

        return new Response(150, "I see that file");
    }
}
