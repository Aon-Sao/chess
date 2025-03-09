package dataaccess;

import model.UserDataRec;

import java.util.*;

public class UserDataAcc implements UserDAO {
    // Singleton
    private static UserDataAcc instance = null;
    private UserDataAcc() {
        clearAll();
    }
    public static UserDataAcc getInstance() {
        if (instance == null) {
            instance = new UserDataAcc();
        }
        return instance;
    }

    private Map<UUID, UserDataRec> users;

    public boolean hasUser(String username) {
        for (var user : listUsers()) {
            if (username.equals(user.username())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createUser(UserDataRec userData) {
        var id = UUID.randomUUID();
        users.put(id, userData);
    }

    @Override
    public Collection<UserDataRec> listUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void clearAll() {
        users = new HashMap<>();
    }
}
