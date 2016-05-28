package pl.pawkrol.academic.ftp.client.message;

import pl.pawkrol.academic.ftp.client.connection.CommandHandler;
import pl.pawkrol.academic.ftp.common.Response;

import java.io.IOException;

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

    public Response execute(CommandHandler commandHandler) throws IOException {
        return commandHandler.execute(this);
    }

    @Override
    public String toString() {
        return command + " " + params + "\n";
    }
}
