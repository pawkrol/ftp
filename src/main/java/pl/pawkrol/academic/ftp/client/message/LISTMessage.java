package pl.pawkrol.academic.ftp.client.message;

/**
 * Created by pawkrol on 5/24/16.
 */
public class LISTMessage extends Message{

    public LISTMessage(String params){
        this.command = "LIST";
        this.params = params;
    }

}
