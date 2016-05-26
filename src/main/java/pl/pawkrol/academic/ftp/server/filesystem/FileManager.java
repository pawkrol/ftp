package pl.pawkrol.academic.ftp.server.filesystem;

import pl.pawkrol.academic.ftp.server.Main;
import pl.pawkrol.academic.ftp.server.db.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pawkrol on 4/23/16.
 */
public class FileManager {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    private String currentDir = "~/";

    public FileManager(DBConnector dbConnector){
        this.userRepository = dbConnector.requestUserRepository();
        this.fileRepository = dbConnector.requestFileRepository();
    }

    public synchronized FTPFile getFTPFile(String filename, User user){
        FTPFile ftpFile = null;
        try {
            DBFile dbFile = fileRepository.getFileByFilename(filename);
            Path path = Paths.get(Main.rootPath.toString(), user.getUsername(), filename);
            File file = new File(path.toString());

            if (dbFile == null || !file.exists()){
                return null;
            }

            User fileUser;
            if ( (fileUser = userRepository.getUserById(dbFile.getUserId())) == null){
                return null;
            }

            ftpFile = new FTPFile(file, fileUser, path, dbFile.isPermRead(),
                                    dbFile.isPermWrite(), file.isDirectory());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ftpFile;
    }

    public synchronized List<FTPFile> getFilesFromDirectory(String dir, User user) throws FileNotFoundException,
                                                                                            NotDirectoryException{
        File file = new File(Main.rootPath.toString() + "/" + user.getUsername() + "/" + dir);
        if (!file.isDirectory()){
            throw new NotDirectoryException();
        }

        List<FTPFile> ftpFiles = new LinkedList<>();

        try {
            List<DBFile> dbFiles = fileRepository.getFilesFromDirectory(dir);
            if (dbFiles.isEmpty()){
                throw new FileNotFoundException();
            }

            for (DBFile dbFile : dbFiles){
                ftpFiles.add(getFTPFile(dbFile.getPath(), user));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ftpFiles;
    }

    public FileReader getFileReader(String filename, User user) throws FileNotFoundException,
            PermissionDeniedException {
        FTPFile ftpFile = getFTPFile(filename, user);
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
}