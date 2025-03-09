package dataaccess;

import model.UserDataRec;

import java.util.Collection;
import java.util.UUID;

public interface UserDAO {
    // Create
    void createUser(UserDataRec userData);

    // Read
    Collection<UserDataRec> listUsers();

    // Delete
    void clearAll();


}
