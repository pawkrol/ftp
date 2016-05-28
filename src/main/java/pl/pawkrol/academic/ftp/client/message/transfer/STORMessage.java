package pl.pawkrol.academic.ftp.client.message.transfer;

/**
 * Created by pawkrol on 5/28/16.
 */
public class STORMessage extends TransferMessage {

    public STORMessage(String params) {
        this.command = "STOR";
        this.params = params;
    }
}
