package dataaccess;

import model.UserDataRec;

import java.util.Collection;
import java.util.UUID;

public interface UserDAO {
    // Create
    void createUser(UserDataRec userData);

    // Read
    UserDataRec getUser(UUID id);
    Collection<UserDataRec> listUsers();

    // Update
    void changeName(UUID id, String username);
    void changePass(UUID id, String pass);
    void changeEmail(UUID id, String email);

    // Delete
    void clearAll();
    void clearUser(UUID id);


}
