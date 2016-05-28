package pl.pawkrol.academic.ftp.client.connection;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by pawkrol on 5/26/16.
 */
public class PassiveDataHandler extends DataHandler {

    private CommandHandler commandHandler;

    public PassiveDataHandler(CommandHandler commandHandler){
        this.commandHandler = commandHandler;
    }

    @Override
    public void open(AddressPortPair addressPortPair) {
        super.open(addressPortPair);

        try {
            dataConnector.init(new Socket(addressPortPair.getAddress(), addressPortPair.getPort()));

            new Thread(dataConnector::connect).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
