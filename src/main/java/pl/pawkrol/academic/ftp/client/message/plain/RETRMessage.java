package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.transfer.TransferMessage;

/**
 * Created by pawkrol on 5/14/16.
 */
public class RETRMessage extends TransferMessage {

    public RETRMessage(String params) {
        this.command = "RETR";
        this.params = params;
    }

}
