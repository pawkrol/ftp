package pl.pawkrol.academic.ftp.client.message.transfer;

import pl.pawkrol.academic.ftp.client.connection.AddressPortPair;
import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.client.connection.DataHandler;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.message.MessageResponsePair;
import pl.pawkrol.academic.ftp.client.message.plain.PASVMessage;
import pl.pawkrol.academic.ftp.client.session.Session;
import pl.pawkrol.academic.ftp.common.Response;

import java.io.IOException;

/**
 * Created by pawkrol on 5/27/16.
 */
public abstract class TransferMessage extends Message {

    @Override
    public Response execute(CommandHandler commandHandler) throws IOException {
        Session.Mode mode = commandHandler.getSession().mode;
        DataHandler dataHandler = commandHandler.getDataHandler();
        AddressPortPair addressPortPair = null;

        if (mode.equals(Session.Mode.PASSIVE)){
            Response response = commandHandler.execute(new PASVMessage());
            commandHandler.propagate(new MessageResponsePair(new PASVMessage(), response));
            if (response.getCode() != 227){
                return super.execute(commandHandler);
            }

            addressPortPair = new AddressPortPair(response.getMessage());
        }

        Response response = super.execute(commandHandler);
        if ((response.getCode() == 150) && (addressPortPair != null)) {
            commandHandler.propagate(new MessageResponsePair(this, response));
            dataHandler.open(addressPortPair);

            response = commandHandler.fetchResponse(); //blocking, desired

            dataHandler.close();
            commandHandler.getSession().setDataHandler(null);
        }

        return response;
    }

}
