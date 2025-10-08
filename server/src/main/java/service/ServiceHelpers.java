package service;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import io.javalin.http.Context;
import model.AuthDataRec;

import java.util.Map;
import java.util.UUID;

public class ServiceHelpers {

    public static boolean isAuthorized(String authToken) {
        for (var auth : new MemoryAuthDAO().listAuths()) {
            if (auth.authToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

    public static String authorize(String username) {
        // TODO: Remember to clear out a user's prior token
        var authData = new MemoryAuthDAO();
        var authToken = UUID.randomUUID().toString();
        authData.addAuth(new AuthDataRec(authToken, username));
        return authToken;
    }

    public static LambdasCanThrow<Context> exceptionWrapper(LambdasCanThrow<Context> func) {
        return (context) -> {
            try {
                func.apply(context);
            } catch (Exception e) {
                var msg = Map.of("message", e.getMessage());
                context.json(new Gson().toJson(msg));
                context.status(500);
            }
        };
    }

    public static LambdasCanThrow<Context> authWrapper(LambdasCanThrow<Context> func) {
        return (context) -> {
            // Unpack pertinent fields from the headers
            var authToken = context.header("authorization");

            // Null checks
            if (authToken == null || authToken.isEmpty()) {
                StockResponses.BAD_REQUEST.apply(context);
                return;
            }

            if (isAuthorized(authToken)) {
                func.apply(context);
            } else {
                StockResponses.UNAUTHORIZED.apply(context);
            }
        };
    }

    public enum StockResponses {
        BAD_REQUEST,
        UNAUTHORIZED,
        ALREADY_TAKEN;

        public void apply(Context context) {
            if (this.equals(BAD_REQUEST)) {
                var msg = Map.of("message", "Error: bad request");
                context.status(400);
                context.json(new Gson().toJson(msg));
            } else if (this.equals(UNAUTHORIZED)) {
                var msg = Map.of("message", "Error: unauthorized");
                context.json(new Gson().toJson(msg));
                context.status(401);
            } else if (this.equals(ALREADY_TAKEN)) {
                var msg = Map.of("message", "Error: already taken");
                context.json(new Gson().toJson(msg));
                context.status(403);
            }
        }
    }

}