package dataaccess;

import model.UserDataRec;

import java.util.Collection;

public interface UserDAO {
    // Create
    void createUser(UserDataRec userData);

    // Read
    Collection<UserDataRec> listUsers();

    // Delete
    void clearAll();


}
