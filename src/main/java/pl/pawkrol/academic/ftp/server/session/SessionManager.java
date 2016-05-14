package pl.pawkrol.academic.ftp.server.session;

import pl.pawkrol.academic.ftp.server.connection.CommandHandler;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.server.db.DBConnector;
import pl.pawkrol.academic.ftp.server.db.UserRepository;
import pl.pawkrol.academic.ftp.server.filesystem.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 2016-03-19.
 */
public class SessionManager {

    private static int id;

    private final Authenticator authenticator;
    private final ConnectionManager connectionManager;
    private final DBConnector dbConnector;

    private List<Session> sessions;

    public SessionManager(ConnectionManager connectionManager, DBConnector dbConnector){
        this.connectionManager = connectionManager;
        this.authenticator = new Authenticator(dbConnector.requestUserRepository());
        this.dbConnector = dbConnector;
        this.sessions = new ArrayList<>();
    }

    public synchronized Session createSession(CommandHandler commandHandler){
        Session session = new Session(this, commandHandler, connectionManager, authenticator,
                                        id++, new FileManager(dbConnector));
        sessions.add(session);
        return session;
    }

    public boolean removeSession(Session session){
        return sessions.remove(session);
    }

}
