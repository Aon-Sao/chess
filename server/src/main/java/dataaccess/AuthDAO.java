package dataaccess;

import model.AuthDataRec;

import java.util.Collection;

public interface AuthDAO {
    // Create
    void createAuth(AuthDataRec authData);

    // Read
    AuthDataRec getAuth(String id);
    Collection<AuthDataRec> listAuths();

    // Update
    void changeName(String id, String username);
    void changeToken(String id, String token);

    // Delete
    void clearAll();
    void clearAuth(String id);


}
