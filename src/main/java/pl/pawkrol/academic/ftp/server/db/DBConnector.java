package pl.pawkrol.academic.ftp.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by pawkrol on 4/23/16.
 */
public class DBConnector {
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://127.0.0.1/ftp";

    private final String USERNAME = "ftp-server";
    private final String PASSWORD = "ftp-password";

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public DBConnector() throws ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        fileRepository = new FileRepository(this);
        userRepository = new UserRepository(this);
    }

    public synchronized Connection makeConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public synchronized FileRepository requestFileRepository(){
        return fileRepository;
    }

    public synchronized UserRepository requestUserRepository(){
        return userRepository;
    }
}
