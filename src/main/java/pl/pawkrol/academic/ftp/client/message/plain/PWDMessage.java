package pl.pawkrol.academic.ftp.client.message.plain;

import pl.pawkrol.academic.ftp.client.message.Message;

/**
 * Created by pawkrol on 5/14/16.
 */
public class PWDMessage extends Message {

    public PWDMessage() {
        command = "PWD";
    }
}
