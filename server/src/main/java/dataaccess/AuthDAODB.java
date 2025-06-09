package dataaccess;

import model.AuthDataRec;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class AuthDAODB implements AuthDAO {

    public AuthDAODB() {

    }

    @Override
    public void addAuth(AuthDataRec data) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("INSERT INTO AuthData (authToken, username) VALUES (?, ?)");
            statement.setString(1, data.authToken());
            statement.setString(2, data.username());
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthDataRec getAuthByToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("SELECT authToken, username FROM AuthData WHERE authToken=?");
            statement.setString(1, authToken);
            var results = statement.executeQuery();
            results.next();
            return new AuthDataRec(results.getString("authToken"), results.getString("username"));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Collection<AuthDataRec> listAuths() throws DataAccessException {
        var auths = new ArrayList<AuthDataRec>();
        try (var conn = DatabaseManager.getConnection()) {
            var results = conn.prepareStatement("SELECT authToken, username FROM AuthData").executeQuery();
            while (results.next()) {
                auths.add(new AuthDataRec(
                        results.getString("authToken"),
                        results.getString("username")));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return auths;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("DELETE FROM AuthData WHERE authToken=?");
            statement.setString(1, authToken);
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            conn.prepareStatement("TRUNCATE AuthData").execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}