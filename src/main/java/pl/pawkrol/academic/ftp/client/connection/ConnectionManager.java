package pl.pawkrol.academic.ftp.client.connection;

import pl.pawkrol.academic.ftp.client.RawResponseDispatcher;
import pl.pawkrol.academic.ftp.client.message.MessageProcessor;
import pl.pawkrol.academic.ftp.client.message.QUITMessage;
import pl.pawkrol.academic.ftp.client.session.Session;
import pl.pawkrol.academic.ftp.client.session.User;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pawkrol on 4/25/16.
 */
public class ConnectionManager {

    private final int port;
    private final String address;
    private final Socket socket;
    private final Session session;
    private final CommandHandler commandHandler;
    private final MessageProcessor messageProcessor;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ConnectionManager(String address, int port, User user) throws IOException {
        this.port = port;
        this.address = address;
        this.socket = new Socket(address, port);
        this.session = new Session(this, user);
        this.commandHandler = new CommandHandler(socket, session);
        this.messageProcessor = new MessageProcessor(commandHandler, executorService);
        messageProcessor.registerMessageResponseListener(new RawResponseDispatcher()::dispatch);

        commandHandler.init();
    }

    //Graceful close
    public void close(){
        messageProcessor.sendMessage(new QUITMessage(), response -> kill());
    }

    public void kill(){
        try {
            socket.close();
            session.close();

            executorService.shutdown();
            executorService.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public MessageProcessor getMessageProcessor() {
        return messageProcessor;
    }
}
