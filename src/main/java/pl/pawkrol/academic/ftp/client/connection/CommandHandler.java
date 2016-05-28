package pl.pawkrol.academic.ftp.client.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.client.message.AUTHMessage;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.message.MessageResponsePair;
import pl.pawkrol.academic.ftp.client.session.Session;
import pl.pawkrol.academic.ftp.common.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by pawkrol on 5/14/16.
 */
public class CommandHandler{

    private final Socket socket;
    private final Session session;
    private final ExecutorService executorService;
    private final ConnectionManager connectionManager;

    private BufferedReader reader;
    private OutputStream serverOutputStream;
    private List<MessageResponseListener> messageResponseListeners;

    public CommandHandler(Socket socket, Session session, ExecutorService executorService,
                          ConnectionManager connectionManager) {
        this.socket = socket;
        this.session = session;
        this.executorService = executorService;
        this.connectionManager = connectionManager;

        messageResponseListeners = new LinkedList<>();
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

    public synchronized Response fetchResponse() throws IOException {
        return new Response(reader.readLine());
    }

    public synchronized Response execute(Message message) throws IOException {
        sendRawMessage(message.toString());
        return fetchResponse();
    }

    //Do not make nested calls, deadlock may happen
    public synchronized void sendMessage(Message message, ResponseListener listener){
        if (socket.isClosed()) return;

        executorService.submit(() -> {
            try {
                Response response = message.execute(this);

                if (listener != null) {
                    listener.onResponse(response);
                }

                propagate(new MessageResponsePair(message, response));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void propagate(MessageResponsePair messageResponsePair){
        for (MessageResponseListener m: messageResponseListeners){
            m.onMessageResponse(messageResponsePair);
        }
    }

    public void registerMessageResponseListener(MessageResponseListener messageResponseListener){
        messageResponseListeners.add(messageResponseListener);
    }

    public Session getSession() {
        return session;
    }

    public DataHandler getDataHandler(){
        return connectionManager.getDataHandler();
    }

    private void sendRawMessage(String message) throws IOException {
        serverOutputStream.write(message.getBytes());
        serverOutputStream.flush();
    }

    @FunctionalInterface
    public interface ResponseListener{
        void onResponse(Response response);
    }

    @FunctionalInterface
    public interface MessageResponseListener{
        void onMessageResponse(MessageResponsePair messageResponsePair);
    }

}
