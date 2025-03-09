package dataaccess;

import model.AuthDataRec;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public String findAuthByAuthToken(String authToken) {
        AtomicReference<String> key = new AtomicReference<>();
        auths.forEach((k, v) -> {
            if (v.authToken().equals(authToken)) {
                key.set(k);
            }
        });
        return key.get();
    }

    public boolean hasAuth(String authToken) {
        for (var auth : listAuths()) {
            if (auth.authToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUser(String username) {
        for (var auth : listAuths()) {
            if (auth.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createAuth(AuthDataRec authData) {
        var id = UUID.randomUUID().toString();
        auths.put(id, authData);
    }

    @Override
    public Collection<AuthDataRec> listAuths() {
        return new ArrayList<>(auths.values());
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
