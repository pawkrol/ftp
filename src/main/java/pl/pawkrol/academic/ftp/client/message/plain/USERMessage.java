package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.Message;

/**
 * Created by pawkrol on 5/14/16.
 */
public class USERMessage extends Message {

    public USERMessage(String params) {
        this.command = "USER";
        this.params = params;
    }

}
