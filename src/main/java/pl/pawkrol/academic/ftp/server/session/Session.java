package pl.pawkrol.academic.ftp.server.session;

import pl.pawkrol.academic.ftp.server.db.User;

/**
 * Created by Pawel on 2016-03-19.
 */
public class Session {

    public enum State {
        NOT_AUTHENTICATED, AUTHENTICATED, AUTHENTICATING
    }

    public State state;

    private final Authenticator authenticator;

    private int id;
    private User user;

    public Session(Authenticator authenticator, int id) {
        this.id = id;
        this.authenticator = authenticator;
        this.state = State.NOT_AUTHENTICATED;
        this.user = new User();
    }

    public boolean authenticate(){
        if (authenticator.authenticate(user.getUsername(), user.getPassword())){

            state = State.AUTHENTICATED;
            return true;
        }

        return false;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public State getState() {
        return state;
    }
}
