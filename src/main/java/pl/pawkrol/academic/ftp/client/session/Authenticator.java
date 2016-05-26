package pl.pawkrol.academic.ftp.client.session;

import pl.pawkrol.academic.ftp.client.message.MessageProcessor;
import pl.pawkrol.academic.ftp.client.message.PASSMessage;
import pl.pawkrol.academic.ftp.client.message.USERMessage;
import pl.pawkrol.academic.ftp.common.Response;

/**
 * Created by pawkrol on 5/15/16.
 */
public class Authenticator {

    private final MessageProcessor messageProcessor;

    private boolean authenticated;
    private User user;

    public Authenticator(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    public boolean authenticate(User user){
        authenticated = false;
        this.user = user;

        messageProcessor.sendMessage(new USERMessage(user.getUsername()), this::onUsername);

        return authenticated;
    }

    private void onUsername(Response response){
        int code = response.getCode();

        if (code == 331){
            messageProcessor.sendMessage(

                    new PASSMessage(user.getPassword()), passResponse -> {
                         if (passResponse.getCode() == 230){
                             authenticated = true;
                         }
                    }

            );
        }
    }
}
