package dataaccess;

import model.UserDataRec;

import java.util.*;

public class UserDataAcc implements UserDAO {
    // Singleton
    private UserDataAcc instance = null;
    private UserDataAcc() {
        clearAll();
    }
    public UserDataAcc getInstance() {
        if (instance == null) {
            instance = new UserDataAcc();
        }
        return instance;
    }

    private Map<UUID, UserDataRec> users;

    @Override
    public void createUser(UserDataRec userData) {
        var id = UUID.randomUUID();
        users.put(id, userData);
    }

    @Override
    public UserDataRec getUser(UUID id) {
        return users.get(id);
    }

    @Override
    public Collection<UserDataRec> listUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void changeName(UUID id, String username) {
        var user = getUser(id);
        user = user.changeUsername(username);
        users.put(id, user);
    }

    @Override
    public void changePass(UUID id, String pass) {
        var user = getUser(id);
        user = user.changePassword(pass);
        users.put(id, user);
    }

    @Override
    public void changeEmail(UUID id, String email) {
        var user = getUser(id);
        user = user.changeEmail(email);
        users.put(id, user);
    }

    @Override
    public void clearAll() {
        users = new HashMap<>();
    }

    @Override
    public void clearUser(UUID id) {
        users.remove(id);
    }
}
