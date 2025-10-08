package service;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Map;

public class ServiceHelpers {

    public static void clearAll() {
        throw new RuntimeException("NYI");
    }

    // Optional context argument to match arity
    public static void clearAll(Context context) {
        clearAll();
    }

    public static boolean isAuthorized(Context context) {
        throw new RuntimeException("NYI");
    }

    public static Handler exceptionWrapper(LambdasCanThrow<Context> func) {
        return (context) -> {
            try {
                func.apply(context);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }

    public static Handler authWrapper(LambdasCanThrow<Context> func) {
        return (context) -> {
            if (isAuthorized(context)) {
                func.apply(context);
            } else {
                throw new RuntimeException("NYI");
            }
        };
    }

}