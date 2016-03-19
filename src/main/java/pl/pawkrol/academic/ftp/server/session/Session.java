package pl.pawkrol.academic.ftp.server.session;

/**
 * Created by Pawel on 2016-03-19.
 */
public class Session {

    public enum State {
        NOT_AUTHENTICATED, AUTHENTICATED
    };

    public State state;

    private final Authenticator authenticator;

    private int id;
    private String username;
    private String password;

    public Session(Authenticator authenticator, int id) {
        this.id = id;
        this.authenticator = authenticator;
        this.state = State.NOT_AUTHENTICATED;
    }

    public boolean authenticate(String username, String password){
        if (authenticator.authenticate(username, password)){
            this.username = username;
            this.password = password;

            state = State.AUTHENTICATED;
            return true;
        }

        return false;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public State getState() {
        return state;
    }
}
