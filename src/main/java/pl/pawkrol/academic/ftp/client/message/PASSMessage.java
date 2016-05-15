package pl.pawkrol.academic.ftp.client.message;

/**
 * Created by pawkrol on 5/14/16.
 */
public class PASSMessage extends Message{

    public PASSMessage(String params) {
        this.command = "PASS";
        this.params = params;
    }
}
