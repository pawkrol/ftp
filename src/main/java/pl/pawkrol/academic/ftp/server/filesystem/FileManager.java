package pl.pawkrol.academic.ftp.server.filesystem;

import org.apache.commons.io.FileUtils;
import pl.pawkrol.academic.ftp.client.session.*;
import pl.pawkrol.academic.ftp.server.Main;
import pl.pawkrol.academic.ftp.server.db.*;
import pl.pawkrol.academic.ftp.server.db.User;

import java.io.*;
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

    private String currentDir = "/";

    public FileManager(DBConnector dbConnector){
        this.userRepository = dbConnector.requestUserRepository();
        this.fileRepository = dbConnector.requestFileRepository();
    }

    public synchronized boolean removeFile(String filename, User user) throws SQLException{
        File file = new File(constructFilePath(filename, user));
        if (!file.isDirectory()) {
            fileRepository.removeFileRecord(filename);
            return file.delete();
        } else {
            return false;
        }
    }

    public synchronized void removeDirectory(String filename, User user) throws SQLException, IOException {
        fileRepository.removeFileRecord(filename);
        File file = new File(constructFilePath(filename, user));
        FileUtils.deleteDirectory(file);
    }


    public synchronized void putFileRecord(String name, boolean writable, boolean readable, User user)
            throws SQLException{
        fileRepository.putFileRecord(name, writable, readable, user);
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

            ftpFile = new FTPFile(file, fileUser, dbFile.getPath(), dbFile.isPermRead(),
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

    public synchronized FileInputStream getFileInputStream(String filename, User user) throws FileNotFoundException,
                                                        PermissionDeniedException, IsDirectoryException{
        FTPFile ftpFile = getFTPFile(filename, user);
        if (ftpFile == null){
            throw new FileNotFoundException();
        }

        if (ftpFile.isDirectory()){
            throw new IsDirectoryException();
        }

        if (!canUserAccessFile(user, ftpFile)){
            throw new PermissionDeniedException("User has no permission to access file");
        }

        return new FileInputStream(ftpFile.getFile());
    }

    public void changeDir(String dir, User user) throws NotDirectoryException,
                                                    FileDoesNotExistsException{
        if (!dir.equals("/")) {
            FTPFile file = getFTPFile(dir, user);
            if (file == null) {
                throw new FileDoesNotExistsException();
            } else if (!file.isDirectory()) {
                throw new NotDirectoryException();
            }
        }

        this.currentDir = dir;
    }

    public String getCurrentDir(){
        return currentDir;
    }

    private boolean canUserAccessFile(User user, FTPFile ftpFile){
        return ftpFile.getUser() == null || (ftpFile.getUser().equals(user)
                && ftpFile.isPermRead());
    }

    public String constructFilePath(String filename, User user){
        String fileDir = Main.rootPath.toString() + "/"
                + user.getUsername() + "/" + currentDir;
        if (currentDir.endsWith("/")){
            return fileDir + filename;
        } else {
            return fileDir + "/" + filename;
        }
    }
}
