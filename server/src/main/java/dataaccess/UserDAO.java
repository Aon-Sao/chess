package dataaccess;

import model.UserDataRec;

import java.sql.SQLException;
import java.util.Collection;

public interface UserDAO {
    void createUser(UserDataRec data) throws DataAccessException;
    Collection<UserDataRec> listUsers() throws DataAccessException;
    void removeUser(String username) throws DataAccessException;
    void clearAll() throws DataAccessException;
}
