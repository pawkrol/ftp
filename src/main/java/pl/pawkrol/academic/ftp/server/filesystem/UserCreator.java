package pl.pawkrol.academic.ftp.server.filesystem;

import pl.pawkrol.academic.ftp.common.User;
import pl.pawkrol.academic.ftp.server.Main;
import pl.pawkrol.academic.ftp.server.db.DBConnector;
import pl.pawkrol.academic.ftp.server.db.FileRepository;
import pl.pawkrol.academic.ftp.server.db.UserRepository;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by pawkrol on 5/29/16.
 */
public class UserCreator {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public UserCreator(DBConnector dbConnector){
        fileRepository = dbConnector.requestFileRepository();
        userRepository = dbConnector.requestUserRepository();
    }

    public boolean create(User user) throws SQLException {
        String rootPath = Main.rootPath.toString() + "/" + user.getUsername();
        File homeFile = new File(rootPath);
        if (!homeFile.mkdir()){
            return false;
        }

        userRepository.addNewUser(user);
        return true;
    }
}
