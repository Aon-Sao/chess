package dataaccess;

import model.UserDataRec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryUserDAO implements UserDAO {

    private static List<UserDataRec> users = new ArrayList<>();

    private static UserDataRec findUser(String username) throws DataAccessException {
        for (var user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("No such username");
    }

    public MemoryUserDAO() {

    }

    @Override
    public void createUser(UserDataRec data) {
        users.add(data);
    }

    @Override
    public Collection<UserDataRec> listUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void removeUser(String username) throws DataAccessException {
        users.remove(findUser(username));
    }

    @Override
    public void clearAll() {
        users = new ArrayList<>();
    }
}
