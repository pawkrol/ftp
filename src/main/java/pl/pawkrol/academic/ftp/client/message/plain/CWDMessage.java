package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.Message;

/**
 * Created by pawkrol on 5/27/16.
 */
public class CWDMessage extends Message{

    public CWDMessage(String params){
        this.command = "CWD";
        this.params = params;
    }
}
