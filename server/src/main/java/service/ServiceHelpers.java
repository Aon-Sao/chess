package service;

import dataaccess.AuthDataAcc;
import model.AuthDataRec;

import java.util.UUID;

public class ServiceHelpers {
    public static ServiceMessage clearAll(ServiceMessage request) {
        UserService.clear();
        GameService.clear();
        return ServiceMessage.builder()
                .setStatusCode(200)
                .build();
    }

    protected static String authorize(String username) {
        var authData = AuthDataAcc.getInstance();
        var authToken = UUID.randomUUID().toString();
        authData.createAuth(new AuthDataRec(authToken, username));
        return authToken;
    }

    public static boolean isAuthorized(ServiceMessage msg) {
        for (var auth : AuthDataAcc.getInstance().listAuths()) {
            if (auth.equals(new AuthDataRec(msg.authToken(), msg.username()))) {
                return true;
            }
        }
        return false;
    }
}
