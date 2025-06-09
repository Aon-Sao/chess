package service;

import dataaccess.AuthDAOMem;
import dataaccess.DataAccessException;
import dataaccess.UserDAODB;
import dataaccess.UserDAOMem;
import model.UserDataRec;

import static service.ServiceHelpers.authorize;

public class UserService {

    public static void clear() throws DataAccessException {
        new UserDAODB().clearAll();
    }

    public static ServiceMessage register(ServiceMessage request) throws DataAccessException {
        var username = request.username();
        var password = request.password();
        var email = request.email();

        if ((username == null || username.isEmpty())
                || (password == null || password.isEmpty())
                || email.isEmpty()) {
            return ServiceHelpers.StockResponses.BAD_REQUEST.value();
        }

        var userData = new UserDAODB();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username)
                || user.email().equals(email)) {
                // Username or email in use
                return ServiceHelpers.StockResponses.ALREADY_TAKEN.value();
            }
        }
        // Good to go
        userData.createUser(new UserDataRec(username, password, email));
        var authToken = authorize(username);
        return ServiceMessage.builder()
                .setUsername(username)
                .setAuthToken(authToken)
                .setStatusCode(200)
                .build();
    }

    public static ServiceMessage login(ServiceMessage request) throws DataAccessException {
        var username = request.username();
        var password = request.password();

        if ((username == null || username.isEmpty())
                || (password == null || password.isEmpty())) {
            return ServiceHelpers.StockResponses.BAD_REQUEST.value();
        }

        var userData = new UserDAODB();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username)
            && user.password().equals(password)) {
                // Authorized
                var authToken = authorize(username);
                return ServiceMessage.builder()
                        .setUsername(username)
                        .setAuthToken(authToken)
                        .setStatusCode(200)
                        .build();
            }
        }
        return ServiceHelpers.StockResponses.UNAUTHORIZED.value();
    }


    public static ServiceMessage logout(ServiceMessage request) throws DataAccessException {
        return ServiceHelpers.authWrapper((msg) -> {
            var authData = new AuthDAOMem();
            authData.removeAuth(request.authToken());
            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .build();
        }).apply(request);
    }
}
