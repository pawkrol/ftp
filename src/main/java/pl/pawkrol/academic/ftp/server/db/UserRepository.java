package pl.pawkrol.academic.ftp.server.db;

import pl.pawkrol.academic.ftp.common.User;

import java.sql.*;
import java.util.Collection;

/**
 * Created by Pawel on 2016-04-09.
 */
public class UserRepository {

    private final static String TABLE_NAME = "users";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String USER_ID = "id";

    private DBConnector dbConnector;

    public UserRepository(DBConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    public synchronized User authenticate(User user) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        User returnUser = null;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                            + USERNAME + " = \"" + user.getUsername() + "\"";

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()
                && resultSet.getString("password").equals(user.getPassword())){
            user.setUserId(resultSet.getInt("id"));
            returnUser = user;
        }

        statement.close();
        connection.close();

        return returnUser;
    }

    public synchronized void addNewUser(User user) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        String query = "INSERT INTO " + TABLE_NAME + " (" + USERNAME + ", " + PASSWORD + ") " +
                        "VALUES( \"" + user.getUsername() + "\", \"" + user.getPassword() + "\")";

        statement.executeUpdate(query);

        statement.close();
        connection.close();

    }

    public synchronized User getUserById(int id) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        User user = null;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                            + USER_ID + " = \"" + id + "\"";

        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()){
            user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                            resultSet.getString("password"));
        }


        statement.close();
        connection.close();

        return user;
    }

}
