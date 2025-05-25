package dataaccess;

import model.AuthDataRec;

import java.util.Collection;

public interface AuthDAO {
    void addAuth(AuthDataRec data);
    AuthDataRec getAuthByToken(String authToken) throws DataAccessException;
    Collection<AuthDataRec> listAuths();
    void removeAuth(String authToken) throws DataAccessException;
    void clearAll();
}
