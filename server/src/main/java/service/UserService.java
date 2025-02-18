package service;

import dataaccess.AuthDataAcc;
import dataaccess.UserDataAcc;
import model.AuthDataRec;
import model.UserDataRec;

import java.util.UUID;

public class UserService {
    private String authorize(String username) {
        var authData = AuthDataAcc.getInstance();
        var authToken = UUID.randomUUID().toString();
        authData.createAuth(new AuthDataRec(authToken, username));
        return authToken;
    }

    public UserClump register(UserClump request) {
        var username = request.username();
        var password = request.password();
        var email = request.email();

        var userData = UserDataAcc.getInstance();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username)
                || user.email().equals(email)) {
                // Username or email in use
                return UserClump.builder()
                        .setMessage("Error: already taken")
                        .setStatusCode(403)
                        .build();
            }
        }
        // Good to go
        userData.createUser(new UserDataRec(username, password, email));
        var authToken = authorize(username);
        return UserClump.builder()
                .setUsername(username)
                .setAuthToken(authToken)
                .setStatusCode(200)
                .build();
    }

    public UserClump login(UserClump request) {
        var username = request.username();
        var password = request.password();

        var userData = UserDataAcc.getInstance();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username)
            && user.password().equals(password)) {
                // Authorized
                var authToken = authorize(username);
                return UserClump.builder()
                        .setUsername(username)
                        .setAuthToken(authToken)
                        .setStatusCode(200)
                        .build();
            }
        }
        return UserClump.builder()
                .setMessage("Error: unauthorized")
                .setStatusCode(401)
                .build();
    }
    public UserClump logout(UserClump request) {
        var authData = AuthDataAcc.getInstance();
        for (var auth : authData.listAuths()) {
            if (auth.authToken().equals(request.authToken())) {
                // Logout
                authData.clearAuth(auth.authToken());
                return UserClump.builder()
                        .setStatusCode(200)
                        .build();
            }
        }
        // Provided authToken not found in DB
        return UserClump.builder()
                .setStatusCode(401)
                .setMessage("Error: unauthorized")
                .build();
    }
}
