package pl.pawkrol.academic.ftp.server.filesystem;

import pl.pawkrol.academic.ftp.server.Main;
import pl.pawkrol.academic.ftp.server.db.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Created by pawkrol on 4/23/16.
 */
public class FileManager {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    private String currentDir = ".";

    public FileManager(DBConnector dbConnector){
        this.userRepository = dbConnector.requestUserRepository();
        this.fileRepository = dbConnector.requestFileRepository();
    }

    public FTPFile getFTPFile(Path path){
        FTPFile ftpFile = null;
        try {
            DBFile dbFile = fileRepository.getFileByFilename(obtainFileNameFromPath(path));
            File file = new File(path.toString());

            if (dbFile == null || !file.exists()){
                return null;
            }

            User user;
            if ( (user = userRepository.getUserById(dbFile.getUserId())) == null){
                return null;
            }

            ftpFile = new FTPFile(file, user, path, dbFile.isPermRead(),
                                    dbFile.isPermWrite(), file.isDirectory());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ftpFile;
    }

    public FileReader getFileReader(String filename, User user) throws FileNotFoundException,
            PermissionDeniedException {
        FTPFile ftpFile = getFTPFile(Paths.get(Main.rootPath.toString(), user.getUsername(), filename));
        if (ftpFile == null){
            throw new FileNotFoundException();
        }

        if (!canUserAccessFile(user, ftpFile)){
            throw new PermissionDeniedException("User has no permission to access file");
        }

        return new FileReader(ftpFile.getFile());
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public String getCurrentDir(){
        return currentDir;
    }

    private boolean canUserAccessFile(User user, FTPFile ftpFile){
        return ftpFile.getUser() == null || (ftpFile.getUser().equals(user)
                && ftpFile.isPermRead());
    }

    private String obtainFileNameFromPath(Path path){
        return path.getName(path.getNameCount() - 1).toString();
    }
}
