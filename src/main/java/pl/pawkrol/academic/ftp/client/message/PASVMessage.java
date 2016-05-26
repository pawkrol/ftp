package pl.pawkrol.academic.ftp.client.message;

/**
 * Created by pawkrol on 5/14/16.
 */
public class PASVMessage extends Message{

    public PASVMessage() {
        command = "PASV";
    }
}
