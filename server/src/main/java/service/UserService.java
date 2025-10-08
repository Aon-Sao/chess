package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.Context;
import model.UserDataRec;

import java.util.List;
import java.util.Map;

import static service.ServiceHelpers.*;

public class UserService {

    /*
    * Each endpoint corresponds to a .*Handler method
    * Each .*Handler method delegates to a .*Checks method,
    * and, if the checks pass, to a .*Action method
    */

    private static boolean userExists(String username, String email) {
        var userData = new MemoryUserDAO();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username)
                    || user.email().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private static boolean registerChecks(Context context) {
        // Unpack pertinent fields from the request body
        // Note: getRequiredFields performs null checks
        var body = getRequiredFields(context, List.of("username", "password", "email"));
        if (!(boolean) body.get("success")) {
            return false;
        }

        var username = (String) body.get("username");
        var password = (String) body.get("password");

        // Check if username or email is taken
        if (userExists(username, password)) {
            ServiceHelpers.StockResponses.ALREADY_TAKEN.apply(context);
            return false;
        }

        return true;
    }

    private static void registerAction(String username, String password, String email) {
        new MemoryUserDAO().createUser(new UserDataRec(username, password, email));
    }

    public static void registerHandler(Context context) throws DataAccessException {
        exceptionWrapper((cxt) -> {
            if (registerChecks(context)) {
                var body = getRequiredFields(context, List.of("username", "password", "email"));
                registerAction(
                        (String) body.get("username"),
                        (String) body.get("password"),
                        (String) body.get("email")
                );
                loginHandler(context);
            }
        }).apply(context);
    }

    private static boolean loginChecks(Context context) {
        // Unpack pertinent fields from the request body
        var body = getRequiredFields(context, List.of("username", "password"));
        if (!(boolean) body.get("success")) {
            return false;
        }
        var username = (String) body.get("username");
        var password = (String) body.get("password");

        // Verify password
        var userData = new MemoryUserDAO();
        for (var user : userData.listUsers()) {
            if (user.username().equals(username) && user.password().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private static String loginAction(String username) {
        // If this is it for this method... it may not be necessary
        return authorize(username);
    }

    public static void loginHandler(Context context) throws DataAccessException {
        exceptionWrapper((cxt) -> {
            if (loginChecks(context)) {
                var username = (String) getRequiredFields(context, List.of("username", "password")).get("username");
                var authToken = loginAction(username);
                var msg = Map.of("username", username, "authToken", authToken);
                context.json(new Gson().toJson(msg));
                context.status(200);
            } else {
                ServiceHelpers.StockResponses.UNAUTHORIZED.apply(context);
            }
        }).apply(context);
    }

    private static boolean logoutChecks(Context context) {
        var authToken = context.header("authorization");
        return !(authToken == null || authToken.isEmpty());
    }

    private static void logoutAction(String authToken) throws DataAccessException {
        new MemoryAuthDAO().removeAuth(authToken);
    }

    public static void logoutHandler(Context context) throws DataAccessException {
        authWrapper(exceptionWrapper((cxt) -> {
            if (logoutChecks(context)) {
                var authToken = context.header("authorization");
                logoutAction(authToken);
                context.status(200);
            }
        })).apply(context);
    }
}