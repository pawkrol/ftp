package pl.pawkrol.academic.ftp.client.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.session.Session;
import pl.pawkrol.academic.ftp.common.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by pawkrol on 5/14/16.
 */
public class CommandHandler{

    private final Socket socket;
    private final Session session;

    private BufferedReader reader;
    private OutputStream serverOutputStream;

    private Queue<Message> messageQueue;

    public CommandHandler(Socket socket, Session session) {
        this.socket = socket;
        this.session = session;
        this.messageQueue = new LinkedList<>();

        session.setCommandHandler(this);
    }

    public void init() {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            getWelcomeMessage();
            serverOutputStream = socket.getOutputStream();

            session.authenticate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getWelcomeMessage() throws IOException {
        String response = reader.readLine();
        final Logger log = LogManager.getLogger("rawResponseView");
        log.log(Level.INFO, response);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response execute(Message message) throws IOException {
        sendMessage(message.toString());
        return new Response(reader.readLine());
    }

    private void sendMessage(String message) throws IOException {
        serverOutputStream.write(message.getBytes());
        serverOutputStream.flush();
    }

}
