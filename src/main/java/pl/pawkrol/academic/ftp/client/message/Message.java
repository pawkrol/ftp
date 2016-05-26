package pl.pawkrol.academic.ftp.client.message;

/**
 * Created by pawkrol on 5/14/16.
 */
public abstract class Message {

    protected String command = "";
    protected String params = "";

    public void setParams(String params){
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public String getParams() {
        return params;
    }

    @Override
    public String toString() {
        return command + " " + params + "\n";
    }
}
