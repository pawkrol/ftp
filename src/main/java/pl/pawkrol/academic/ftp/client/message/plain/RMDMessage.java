package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.Message;

/**
 * Created by pawkrol on 5/29/16.
 */
public class RMDMessage extends Message{

    public RMDMessage(String params){
        this.command = "RMD";
        this.params = params;
    }
}
