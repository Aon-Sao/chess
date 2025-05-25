package dataaccess;

import model.UserDataRec;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDAOMem implements UserDAO {

    private static List<UserDataRec> users;

    private static UserDataRec findUser(String username) throws DataAccessException {
        for (var user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("No such username");
    }

    public UserDAOMem() {

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
        users = List.of();
    }
}
