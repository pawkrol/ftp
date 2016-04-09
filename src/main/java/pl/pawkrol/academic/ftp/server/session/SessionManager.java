package pl.pawkrol.academic.ftp.server.session;

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
    private List<Session> sessions;

    public SessionManager(){
        this.userRepository = new UserRepository();
        this.authenticator = new Authenticator(userRepository);
        this.sessions = new ArrayList<>();
    }

    public synchronized Session createSession(){
        Session session = new Session(authenticator, id++);
        sessions.add(session);
        return session;
    }

    public boolean removeSession(Session session){
        return sessions.remove(session);
    }

}
