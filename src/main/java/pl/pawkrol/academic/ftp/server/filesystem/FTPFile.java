package pl.pawkrol.academic.ftp.server.filesystem;

import pl.pawkrol.academic.ftp.common.User;

import java.io.File;

/**
 * Created by pawkrol on 4/24/16.
 */
public class FTPFile {

    private File file;
    private User user;
    private String path;

    private boolean permRead;
    private boolean permWrite;
    private boolean directory;

    public FTPFile(File file, User user, String path, boolean permRead, boolean permWrite, boolean directory) {
        this.file = file;
        this.user = user;
        this.path = path;
        this.permRead = permRead;
        this.permWrite = permWrite;
        this.directory = directory;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPermRead() {
        return permRead;
    }

    public void setPermRead(boolean permRead) {
        this.permRead = permRead;
    }

    public boolean isPermWrite() {
        return permWrite;
    }

    public void setPermWrite(boolean permWrite) {
        this.permWrite = permWrite;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }
}
