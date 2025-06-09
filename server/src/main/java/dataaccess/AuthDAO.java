package dataaccess;

import model.AuthDataRec;

import java.util.Collection;

public interface AuthDAO {
    void addAuth(AuthDataRec data) throws DataAccessException;
    AuthDataRec getAuthByToken(String authToken) throws DataAccessException;
    Collection<AuthDataRec> listAuths() throws DataAccessException;
    void removeAuth(String authToken) throws DataAccessException;
    void clearAll() throws DataAccessException;
}
