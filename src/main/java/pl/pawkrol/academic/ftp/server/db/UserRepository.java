package pl.pawkrol.academic.ftp.server.db;

import pl.pawkrol.academic.ftp.common.User;

import java.sql.*;

/**
 * Created by Pawel on 2016-04-09.
 */
public class UserRepository {

    private DBConnector dbConnector;

    public UserRepository(DBConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    public synchronized User authenticate(User user) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        User returnUser = null;

        try {
            String query = "SELECT * FROM users WHERE username = \"" + user.getUsername() + "\"";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()
                    && resultSet.getString("password").equals(user.getPassword())){
                user.setUserId(resultSet.getInt("id"));
                returnUser = user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
            connection.close();
        }

        return returnUser;
    }

    public synchronized User getUserById(int id) throws SQLException {
        Connection connection = dbConnector.makeConnection();
        Statement statement = connection.createStatement();

        User user = null;

        try {
            String query = "SELECT * FROM users WHERE id = \"" + id + "\"";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()){
                user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                                resultSet.getString("password"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
            connection.close();
        }

        return user;
    }

}
