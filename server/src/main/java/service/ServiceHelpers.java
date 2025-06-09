package service;

import dataaccess.AuthDAODB;
import dataaccess.AuthDAOMem;
import dataaccess.DataAccessException;
import model.AuthDataRec;

import java.util.UUID;
import java.util.function.Function;

public class ServiceHelpers {
    public static ServiceMessage clearAll() throws DataAccessException {
        UserService.clear();
        GameService.clear();
        new AuthDAODB().clearAll();
        return ServiceMessage.builder()
                .setStatusCode(200)
                .build();
    }

    // Optionally take an argument, which we discard, for similarity with other service functions
    public static ServiceMessage clearAll(ServiceMessage msg) throws DataAccessException {
        return clearAll();
    }

    public static String getUsernameByAuthToken(String authToken) throws DataAccessException {
        var authData = new AuthDAODB();
        for (var auth : authData.listAuths()) {
            if (auth.authToken().equals(authToken)) {
                return auth.username();
            }
        }
        return null;
    }

    protected static String authorize(String username) throws DataAccessException {
        var authData = new AuthDAODB();
        var authToken = UUID.randomUUID().toString();
        authData.addAuth(new AuthDataRec(authToken, username));
        return authToken;
    }

    public static boolean isAuthorized(ServiceMessage msg) throws DataAccessException {
        for (var auth : new AuthDAODB().listAuths()) {
            // We do not check if username matches, because it is not always provided
            // authTokens should be unique anyway, which is sufficient
            if (auth.authToken().equals(msg.authToken())) {
                return true;
            }
        }
        return false;
    }

    public static LambdasCanThrow<ServiceMessage, ServiceMessage> exceptionWrapper(LambdasCanThrow<ServiceMessage, ServiceMessage> func) {
        return (msg) -> {
            try {
                return (ServiceMessage) func.apply(msg);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }

    public static LambdasCanThrow<ServiceMessage, ServiceMessage> authWrapper(LambdasCanThrow<ServiceMessage, ServiceMessage> func) {
        return (msg) -> {
            if (isAuthorized(msg)) {
                return func.apply(msg);
            } else {
                return StockResponses.UNAUTHORIZED.value();
            }
        };
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
