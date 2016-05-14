package pl.pawkrol.academic.ftp.server.db;


/**
 * Created by Pawel on 2016-04-09.
 */
public class User {

    private int userId;
    private String username;
    private String password;

    public User(){}

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return (user.getUserId() == userId) && (user.getPassword().equals(password))
                && (user.getUsername().equals(username));
    }

    @Override
    public String toString() {
        return "User (" + username + ')';
    }
}
