package service;

import dataaccess.AuthDataAcc;
import dataaccess.UserDataAcc;
import model.AuthDataRec;
import model.UserDataRec;

import java.util.UUID;

import static service.ServiceHelpers.authorize;
import static service.ServiceHelpers.isAuthorized;

public class UserService {

    public static void clear() {
        UserDataAcc.getInstance().clearAll();
    }

    public static ServiceMessage register(ServiceMessage request) {
        var username = request.username();
        var password = request.password();
        var email = request.email();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return ServiceMessage.builder()
                    .setStatusCode(400)
                    .setMessage("Error: bad request")
                    .build();
        }

        var userData = UserDataAcc.getInstance();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username)
                || user.email().equals(email)) {
                // Username or email in use
                return ServiceMessage.builder()
                        .setMessage("Error: already taken")
                        .setStatusCode(403)
                        .build();
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
        return ServiceMessage.builder()
                .setMessage("Error: unauthorized")
                .setStatusCode(401)
                .build();
    }


    public static ServiceMessage logout(ServiceMessage request) {
        if (isAuthorized(request)) {
            // Logout
            AuthDataAcc.getInstance().clearAuth(request.authToken());
            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .build();
        } else {
            // Provided authToken not found in DB
            return ServiceMessage.builder()
                    .setStatusCode(401)
                    .setMessage("Error: unauthorized")
                    .build();
        }
    }
}
