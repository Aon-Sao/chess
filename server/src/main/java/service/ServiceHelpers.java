package service;

import dataaccess.AuthDataAcc;
import model.AuthDataRec;

import java.util.UUID;

public class ServiceHelpers {
    public static ServiceMessage clearAll(ServiceMessage request) {
        UserService.clear();
        GameService.clear();
        AuthDataAcc.getInstance().clearAll();
        return ServiceMessage.builder()
                .setStatusCode(200)
                .build();
    }

    public static String getUsernameByAuthToken(String authToken) {
        var authData = AuthDataAcc.getInstance();
        for (var auth : authData.listAuths()) {
            if (auth.authToken().equals(authToken)) {
                return auth.username();
            }
        }
        return null;
    }

    protected static String authorize(String username) {
        var authData = AuthDataAcc.getInstance();
        var authToken = UUID.randomUUID().toString();
        authData.createAuth(new AuthDataRec(authToken, username));
        return authToken;
    }

    public static boolean isAuthorized(ServiceMessage msg) {
        for (var auth : AuthDataAcc.getInstance().listAuths()) {
            // We do not check if username matches, because it is not always provided
            // authTokens should be unique anyways, which is sufficient
            if (auth.authToken().equals(msg.authToken())) {
                return true;
            }
        }
        return false;
    }

    public enum StockResponses {
        BAD_REQUEST(ServiceMessage.builder()
                .setStatusCode(400)
                .setMessage("Error: bad request")
                .build()),
        UNAUTHORIZED(ServiceMessage.builder()
                .setStatusCode(401)
                .setMessage("Error: unauthorized")
                .build()),
        ALREADY_TAKEN(ServiceMessage.builder()
                .setStatusCode(403)
                .setMessage("Error: already taken")
                .build());

        StockResponses(ServiceMessage msg) {
            this.msg = msg;
        }
        private final ServiceMessage msg;
        public ServiceMessage value() {
            return msg;
        }
    }
}
