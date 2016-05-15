package pl.pawkrol.academic.ftp.client.message;

import pl.pawkrol.academic.ftp.common.Response;

/**
 * Created by pawkrol on 5/15/16.
 */
public final class MessageResponsePair {

    private final Message message;
    private final Response response;

    public MessageResponsePair(Message message, Response response) {
        this.message = message;
        this.response = response;
    }

    public Message getMessage() {
        return message;
    }

    public Response getResponse() {
        return response;
    }
}
