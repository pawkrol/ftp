package pl.pawkrol.academic.ftp.server.session;

import pl.pawkrol.academic.ftp.server.connection.CommandHandler;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.server.db.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 2016-03-19.
 */
public class SessionManager {

    private static int id;

    private final Authenticator authenticator;
    private final UserRepository userRepository;
    private final ConnectionManager connectionManager;

    private List<Session> sessions;

    public SessionManager(ConnectionManager connectionManager){
        this.userRepository = new UserRepository();
        this.connectionManager = connectionManager;
        this.authenticator = new Authenticator(userRepository);
        this.sessions = new ArrayList<>();
    }

    public synchronized Session createSession(CommandHandler commandHandler){
        Session session = new Session(this, commandHandler, connectionManager, authenticator, id++);
        sessions.add(session);
        return session;
    }

    public boolean removeSession(Session session){
        return sessions.remove(session);
    }

}
