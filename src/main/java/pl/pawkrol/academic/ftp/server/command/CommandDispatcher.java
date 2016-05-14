package pl.pawkrol.academic.ftp.server.command;

import pl.pawkrol.academic.ftp.server.connection.Response;
import pl.pawkrol.academic.ftp.server.session.Session;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Pawel on 2016-03-20.
 */
public class CommandDispatcher {

    private final Session session;

    private HashMap<String, Command> commands = new HashMap<>();

    public CommandDispatcher(Session session){
        this.session = session;

        registerCommands();
    }

    public Response dispatch(String commandString){
        String[] params = commandString.split(" ");

        Command command = getCommand(params[0]);

        if (command == null){
            return new Response(501, "Unknown command " + params[0] + ".");
        }

        if ( !( params[0].equals("USER") || params[0].equals("PASS") || params[0].equals("QUIT") )
                                        && (session.getState() == Session.State.NOT_AUTHENTICATED) ){
            return new Response(332, "Need account/unauthorized.");
        }

        return command.execute(Arrays.copyOfRange(params, 1, params.length));
    }

    private Command getCommand(String command){
        return commands.get(command);
    }

    private void registerCommands(){
        commands.put("USER", new USERCommand(session));
        commands.put("PASS", new PASSCommand(session));
        commands.put("QUIT", new QUITCommand(session));
        commands.put("PASV", new PASVCommand(session));
        commands.put("RETR", new RETRCommand(session));
        commands.put("PWD", new PWDCommand(session));
    }

}
