package pl.pawkrol.academic.ftp.server.db;

import pl.pawkrol.academic.ftp.common.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pawkrol on 4/23/16.
 */
public class FileRepository {

    private final static String TABLE_NAME = "files";

    private final static String FILE_ID = "file_id";
    private final static String FILENAME = "filename";
    private final static String PERM_WRITE = "perm_write";
    private final static String PERM_READ = "perm_read";
    private final static String OWNER_ID = "user_id";

    private DBConnector dbConnector;

    public FileRepository(DBConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    public synchronized void removeFileRecord(String filename, User user) throws SQLException{
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                            + FILENAME + "=\"" + filename + "\""
                            + " AND " + OWNER_ID + "=" + user.getUserId();
        statement.executeUpdate(query);

        connection.close();
        statement.close();
    }

    public synchronized void putFileRecord(String filename, boolean writable,
                                           boolean readable, User user) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        String query = "INSERT INTO " + TABLE_NAME + " (" + FILENAME + ", " + PERM_WRITE + ", "
                                + PERM_READ + ", " + OWNER_ID + ") VALUES(\"" + filename + "\", "
                                + (writable ? "1" : "0") + ", " + (readable ? "1" : "0") + ", "
                                + user.getUserId() + ")";

        statement.executeUpdate(query);

        connection.close();
        statement.close();
    }

    public synchronized DBFile getFileByFilename(String filename, User user) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        DBFile dbFile = null;

        String query = "SELECT " + FILE_ID + ", " + FILENAME + ", " +
                         PERM_WRITE + ", " + PERM_READ + ", " + OWNER_ID +
                            " FROM " + TABLE_NAME +
                            " WHERE " + FILENAME + " = \"" + filename +
                            "\" AND " + OWNER_ID + " = " + user.getUserId();

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()){
            dbFile = getDBFileFromResultSet(resultSet);
        }

        statement.close();
        connection.close();

        return dbFile;
    }

    public synchronized List<DBFile> getFilesFromDirectory(String dir, User user) throws SQLException{
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        List<DBFile> files = new LinkedList<>();

        String query = "SELECT " + FILE_ID + ", " + FILENAME + ", " +
                            PERM_WRITE + ", " + PERM_READ + ", " + OWNER_ID +
                            " FROM " + TABLE_NAME + " WHERE " + FILENAME +
                            " LIKE \"" + dir + "%\" AND " + OWNER_ID + "=" + user.getUserId();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            files.add(getDBFileFromResultSet(resultSet));
        }

        statement.close();
        connection.close();

        return files;
    }

    private DBFile getDBFileFromResultSet(ResultSet resultSet) throws SQLException{
        return new DBFile(resultSet.getInt(FILE_ID), resultSet.getString(FILENAME),
                resultSet.getBoolean(PERM_WRITE), resultSet.getBoolean(PERM_READ),
                resultSet.getInt(OWNER_ID));
    }
}
