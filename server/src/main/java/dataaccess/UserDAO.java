package dataaccess;

import model.UserDataRec;

import java.util.Collection;

public interface UserDAO {
    void createUser(UserDataRec data);
    Collection<UserDataRec> listUsers();
    void removeUser(String username) throws DataAccessException;
    void clearAll();
}
