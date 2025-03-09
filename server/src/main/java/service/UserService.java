package service;

import dataaccess.AuthDataAcc;
import dataaccess.UserDataAcc;
import model.UserDataRec;

import static service.ServiceHelpers.authorize;

public class UserService {

    public static void clear() {
        UserDataAcc.getInstance().clearAll();
    }

    public static ServiceMessage register(ServiceMessage request) {
        var username = request.username();
        var password = request.password();
        var email = request.email();

        if ((username == null || username.isEmpty())
                || (password == null || password.isEmpty())
                || email.isEmpty()) {
            return ServiceHelpers.StockResponses.BAD_REQUEST.value();
        }

        var userData = UserDataAcc.getInstance();
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

    public static ServiceMessage login(ServiceMessage request) {
        var username = request.username();
        var password = request.password();

        var userData = UserDataAcc.getInstance();
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


    public static ServiceMessage logout(ServiceMessage request) {
        var authData = AuthDataAcc.getInstance();
        var authUUID = authData.findAuthByAuthToken(request.authToken());
        authData.clearAuth(authUUID);
            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .build();
    }
}
