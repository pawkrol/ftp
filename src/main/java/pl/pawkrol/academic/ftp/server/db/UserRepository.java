package pl.pawkrol.academic.ftp.server.db;

import java.sql.*;

/**
 * Created by Pawel on 2016-04-09.
 */
public class UserRepository {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.168.0.19/ftp";

    static final String USERNAME = "ftp-server";
    static final String PASSWORD = "ftp-password";

    private Connection connection;
    private Statement statement;

    public UserRepository(){
        try {
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized User authenticate(User user){
        try {
            String query = "SELECT * FROM users WHERE username = \"" + user.getUsername() + "\"";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next() && resultSet.getString("password").equals(user.getPassword())){
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
