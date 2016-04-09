package pl.pawkrol.academic.ftp.server.session;

import pl.pawkrol.academic.ftp.server.db.User;
import pl.pawkrol.academic.ftp.server.db.UserRepository;

/**
 * Created by Pawel on 2016-03-19.
 */
public class Authenticator {

    private final UserRepository userRepository;

    Authenticator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public synchronized boolean authenticate(User user){
        return userRepository.authenticate(user) != null;
    }

}
