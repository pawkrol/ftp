package pl.pawkrol.academic.ftp.server.session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 2016-03-19.
 */
public class SessionManager {

    private static int id;

    private final Authenticator authenticator;
    private List<Session> sessions;

    public SessionManager(){
        this.authenticator = new Authenticator();
        this.sessions = new ArrayList<>();
    }

    public Session createSession(){
        Session session = new Session(authenticator, id++);
        sessions.add(session);
        return session;
    }

    public boolean removeSession(Session session){
        return sessions.remove(session);
    }

}
