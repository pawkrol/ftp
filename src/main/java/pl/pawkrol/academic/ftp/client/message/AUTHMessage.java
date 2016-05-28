package pl.pawkrol.academic.ftp.client.message;

import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.message.plain.PASSMessage;
import pl.pawkrol.academic.ftp.client.message.plain.USERMessage;
import pl.pawkrol.academic.ftp.common.Response;

import java.io.IOException;

/**
 * Created by pawkrol on 5/27/16.
 */
public class AUTHMessage extends Message{

    private final String login;
    private final String password;

    public AUTHMessage(String login, String password) {
        this.command = "PASS";
        this.login = login;
        this.password = password;
    }

    @Override
    public Response execute(CommandHandler commandHandler) throws IOException {
        Message userMsg = new USERMessage(login);
        Response response = commandHandler.execute(userMsg);
        commandHandler.propagate(new MessageResponsePair(userMsg, response));

        if (response.getCode() == 331){
            response = commandHandler.execute(new PASSMessage(password));
        }

        return response;
    }
}
