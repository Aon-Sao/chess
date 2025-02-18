package dataaccess;

import model.AuthDataRec;

import java.util.*;

public class AuthDataAcc implements AuthDAO {
    // Singleton
    private AuthDataAcc instance = null;
    private AuthDataAcc() {
        clearAll();
    }
    public AuthDataAcc getInstance() {
        if (instance == null) {
            instance = new AuthDataAcc();
        }
        return instance;
    }

    private Map<UUID, AuthDataRec> auths;

    @Override
    public void createAuth(AuthDataRec authData) {
        var id = UUID.randomUUID();
        auths.put(id, authData);
    }

    @Override
    public AuthDataRec getAuth(UUID id) {
        return auths.get(id);
    }

    @Override
    public Collection<AuthDataRec> listAuths() {
        return new ArrayList<>(auths.values());
    }

    @Override
    public void changeName(UUID id, String username) {
        var user = getAuth(id);
        user = user.changeUsername(username);
        auths.put(id, user);
    }

    @Override
    public void changeToken(UUID id, String token) {
        var user = getAuth(id);
        user = user.changeToken(token);
        auths.put(id, user);
    }

    @Override
    public void clearAll() {
        auths = new HashMap<>();
    }

    @Override
    public void clearAuth(UUID id) {
        auths.remove(id);
    }
}
