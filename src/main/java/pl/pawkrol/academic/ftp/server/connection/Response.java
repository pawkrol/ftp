package pl.pawkrol.academic.ftp.server.connection;

/**
 * Created by Pawel on 2016-03-20.
 */
public class Response {

    private int number;
    private String message;

    public Response(int number, String message){
        this.number = number;
        this.message = message;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return number + " " + message + "\n";
    }
}
