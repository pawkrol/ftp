package pl.pawkrol.academic.ftp.server.db;

/**
 * Created by pawkrol on 4/23/16.
 */
public class DBFile {

    private int fileId;
    private String path;
    private boolean permWrite;
    private boolean permRead;
    private int userId;

    public DBFile(int fileId, String path, boolean permWrite, boolean permRead, int userId) {
        this.fileId = fileId;
        this.path = path;
        this.permWrite = permWrite;
        this.permRead = permRead;
        this.userId = userId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPermWrite() {
        return permWrite;
    }

    public void setPermWrite(boolean permWrite) {
        this.permWrite = permWrite;
    }

    public boolean isPermRead() {
        return permRead;
    }

    public void setPermRead(boolean permRead) {
        this.permRead = permRead;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
