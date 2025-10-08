package service;

import com.google.gson.Gson;
import io.javalin.http.Context;

import java.util.Map;

public class ServiceHelpers {

    public static boolean isAuthorized(Context context) {
        throw new RuntimeException("NYI");
    }

    public static String authorize(String username) {
        // Remember to clear out a user's prior token
        throw new RuntimeException("NYI");
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
            if (isAuthorized(context)) {
                func.apply(context);
            } else {
                var msg = Map.of("message", "Error: unauthorized");
                context.json(new Gson().toJson(msg));
                context.status(401);
            }
        };
    }

}