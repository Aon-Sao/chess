package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.Context;
import model.UserDataRec;

import java.util.Map;

import static service.ServiceHelpers.*;

public class UserService {

    public static void register(Context context) throws DataAccessException {
        exceptionWrapper((cxt) -> {
            // Unpack pertinent fields from the request body
            Map body = new Gson().fromJson(context.body(), Map.class);
            var username = (String) body.get("username");
            var password = (String) body.get("password");
            var email = (String) body.get("email");

            // Null checks
            if (username == null || username.isEmpty()
                    || password == null || password.isEmpty()
                    || email == null || email.isEmpty()) {
                ServiceHelpers.StockResponses.BAD_REQUEST.apply(context);
                return;
            }

            // Check if username or email is taken
            var userData = new MemoryUserDAO();
            for (var user : userData.listUsers()) {
                if (user.username().equals(username)
                    || user.email().equals(email)) {
                    ServiceHelpers.StockResponses.ALREADY_TAKEN.apply(context);
                    return;
                }
            }

            // Good to go, register the user and log them in
            userData.createUser(new UserDataRec(username, password, email));
            login(context);

        }).apply(context);
    }

    public static void login(Context context) throws DataAccessException {
        exceptionWrapper((cxt) -> {
            // Unpack pertinent fields from the request body
            Map body = new Gson().fromJson(context.body(), Map.class);
            var username = (String) body.get("username");
            var password = (String) body.get("password");

            // Null checks
            if (username == null || username.isEmpty()
                    || password == null || password.isEmpty()) {
                ServiceHelpers.StockResponses.BAD_REQUEST.apply(context);
                return;
            }

            // Verify password
            var userData = new MemoryUserDAO();
            for (var user : userData.listUsers()) {
                if (user.username().equals(username) && user.password().equals(password)) {
                    var authToken = authorize(username);
                    var msg = Map.of("username", username, "authToken", authToken);
                    context.json(new Gson().toJson(msg));
                    context.status(200);
                    return;
                }
            }

            // If we got this far, the user could not be authorized
            ServiceHelpers.StockResponses.UNAUTHORIZED.apply(context);
            return;

        }).apply(context);
    }

    public static void logout(Context context) throws DataAccessException {
        authWrapper(exceptionWrapper((cxt) -> {
            // Delete authToken
            var authToken = context.header("authorization");
            var authData = new MemoryAuthDAO();
            authData.removeAuth(authToken);
            context.status(200);

        })).apply(context);
    }
}