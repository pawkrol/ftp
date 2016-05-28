package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.Message;

/**
 * Created by pawkrol on 5/29/16.
 */
public class DELEMessage extends Message{

    public DELEMessage(String params){
        this.command = "DELE";
        this.params = params;
    }

}
