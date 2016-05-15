package pl.pawkrol.academic.ftp.client.message;

/**
 * Created by pawkrol on 5/14/16.
 */
public class QUITMessage extends Message{

    public QUITMessage() {
        command = "QUIT";
    }
}
