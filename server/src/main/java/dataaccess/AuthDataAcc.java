package dataaccess;

import model.AuthDataRec;

import java.util.*;

public class AuthDataAcc implements AuthDAO {
    // Singleton
    private static AuthDataAcc instance = null;
    private AuthDataAcc() {
        clearAll();
    }
    public static AuthDataAcc getInstance() {
        if (instance == null) {
            instance = new AuthDataAcc();
        }
        return instance;
    }

    private Map<String, AuthDataRec> auths;

    @Override
    public void createAuth(AuthDataRec authData) {
        var id = UUID.randomUUID().toString();
        auths.put(id, authData);
    }

    @Override
    public AuthDataRec getAuth(String id) {
        return auths.get(id);
    }

    @Override
    public Collection<AuthDataRec> listAuths() {
        return new ArrayList<>(auths.values());
    }

    @Override
    public void changeName(String id, String username) {
        var user = getAuth(id);
        user = user.changeUsername(username);
        auths.put(id, user);
    }

    @Override
    public void changeToken(String id, String token) {
        var user = getAuth(id);
        user = user.changeToken(token);
        auths.put(id, user);
    }

    @Override
    public void clearAll() {
        auths = new HashMap<>();
    }

    @Override
    public void clearAuth(String id) {
        auths.remove(id);
    }
}
