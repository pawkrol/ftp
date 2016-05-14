package pl.pawkrol.academic.ftp.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public DBFile getFileByFilename(String filename) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        DBFile dbFile = null;

        try {
            String query = "SELECT " + FILE_ID + ", " + FILENAME + ", " +
                             PERM_WRITE + ", " + PERM_READ + ", " + OWNER_ID +
                                " FROM " + TABLE_NAME + " WHERE " + FILENAME + " = \"" + filename + "\"";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()){
                dbFile = new DBFile(resultSet.getInt(FILE_ID), resultSet.getString(FILENAME),
                                        resultSet.getBoolean(PERM_WRITE), resultSet.getBoolean(PERM_READ),
                                        resultSet.getInt(OWNER_ID));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
            connection.close();
        }

        return dbFile;
    }

}
