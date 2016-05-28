package pl.pawkrol.academic.ftp.client;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawkrol.academic.ftp.client.message.Message;
import pl.pawkrol.academic.ftp.client.message.MessageResponsePair;
import pl.pawkrol.academic.ftp.common.Response;

/**
 * Created by pawkrol on 5/14/16.
 */
public class RawResponseDispatcher {

    private static final Logger log = LogManager.getLogger("rawResponseView");

    public synchronized boolean dispatch(MessageResponsePair messageResponsePair){
        Response response = messageResponsePair.getResponse();
        Message message = messageResponsePair.getMessage();

        int code = response.getCode();
        String logMsg = formatLog(message.getCommand(), response.toString());

        if (code < 300){
            log.log(Level.INFO, logMsg);
            return true;
        } else if (code >= 300 && code < 500){
            log.log(Level.WARN, logMsg);
            return false;
        } else if (code >= 500 && code < 600){
            log.log(Level.ERROR, logMsg);
            return false;
        }

        return false;
    }

    private synchronized String formatLog(String command, String response){
        return command + ": " + response;
    }
}
