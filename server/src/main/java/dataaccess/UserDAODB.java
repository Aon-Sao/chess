package dataaccess;

import model.UserDataRec;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserDAODB implements UserDAO {

    public UserDAODB() {

    }

    @Override
    public void createUser(UserDataRec data) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)");
            statement.setString(1, data.username());
            statement.setString(2, data.password());
            statement.setString(3, data.email());
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Collection<UserDataRec> listUsers() throws DataAccessException {
        var users = new ArrayList<UserDataRec>();
        try (var conn = DatabaseManager.getConnection()) {
            var results = conn.prepareStatement("SELECT username, password, email FROM UserData").executeQuery();
            while (results.next()) {
                users.add(new UserDataRec(
                        results.getString("username"),
                        results.getString("password"),
                        results.getString("email")));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return users;
    }

    @Override
    public void removeUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("DELETE FROM UserData WHERE username=?");
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            conn.prepareStatement("TRUNCATE UserData").execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}