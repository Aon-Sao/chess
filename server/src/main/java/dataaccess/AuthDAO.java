package dataaccess;

import model.AuthDataRec;

import java.util.Collection;
import java.util.UUID;

public interface AuthDAO {
    // Create
    void createAuth(AuthDataRec authData);

    // Read
    AuthDataRec getAuth(UUID id);
    Collection<AuthDataRec> listAuths();

    // Update
    void changeName(UUID id, String username);
    void changeToken(UUID id, String token);

    // Delete
    void clearAll();
    void clearAuth(UUID id);


}
