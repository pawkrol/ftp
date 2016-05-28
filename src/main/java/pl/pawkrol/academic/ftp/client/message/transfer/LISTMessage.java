package pl.pawkrol.academic.ftp.client.message.transfer;

/**
 * Created by pawkrol on 5/24/16.
 */
public class LISTMessage extends TransferMessage {

    public LISTMessage(String params){
        this.command = "LIST";
        this.params = params;
    }

}
