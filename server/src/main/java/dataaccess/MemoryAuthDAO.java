package dataaccess;

import model.AuthDataRec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO {

    private static List<AuthDataRec> auths = new ArrayList<>();

    private static AuthDataRec findAuth(String authToken) throws DataAccessException {
        for (var auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        throw new DataAccessException("No such authToken");
    }

    public MemoryAuthDAO() {

    }

    @Override
    public void addAuth(AuthDataRec data) {
        auths.add(data);
    }

    @Override
    public AuthDataRec getAuthByToken(String authToken) throws DataAccessException {
        return findAuth(authToken);
    }

    @Override
    public Collection<AuthDataRec> listAuths() {
        return new ArrayList<>(auths);
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        auths.remove(findAuth(authToken));
    }

    @Override
    public void clearAll() {
        auths = new ArrayList<>();
    }
}
