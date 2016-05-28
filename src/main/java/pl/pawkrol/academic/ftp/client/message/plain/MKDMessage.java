package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.Message;

/**
 * Created by pawkrol on 5/28/16.
 */
public class MKDMessage extends Message{

    public MKDMessage(String params){
        this.command = "MKD";
        this.params = params;
    }

}
