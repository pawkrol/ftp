package pl.pawkrol.academic.ftp.client.message;

import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.common.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by pawkrol on 5/15/16.
 */
public class MessageProcessor {

    private final CommandHandler commandHandler;
    private final ExecutorService executorService;

    private List<MessageResponseListener> messageResponseListeners;

    public MessageProcessor(CommandHandler commandHandler, ExecutorService executorService) {
        this.commandHandler = commandHandler;
        this.executorService = executorService;

        messageResponseListeners = new LinkedList<>();
    }

    public synchronized void sendMessage(Message message, ResponseListener listener){
        executorService.submit(() -> {
            try {
                Response response = commandHandler.execute(message);

                if (listener != null) {
                    listener.onResponse(response);
                }

                for (MessageResponseListener m: messageResponseListeners){
                    m.onMessageResponse(new MessageResponsePair(message, response));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void registerMessageResponseListener(MessageResponseListener messageResponseListener){
        messageResponseListeners.add(messageResponseListener);
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

