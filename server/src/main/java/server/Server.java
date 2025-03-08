package server;

import com.google.gson.Gson;
import service.GameService;
import service.ServiceHelpers;
import service.ServiceMessage;
import service.UserService;
import spark.*;

import java.util.function.Function;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db",      genericHandler(ServiceHelpers::clearAll));
        Spark.post(  "/user",    genericHandler(UserService::register));
        Spark.post(  "/session", genericHandler(UserService::login));
        Spark.delete("/session", genericHandler(UserService::logout));
        Spark.get(   "/game",    genericHandler(GameService::listGames));
        Spark.post(  "/game",    genericHandler(GameService::createGame));
        Spark.put(   "/game",    genericHandler(GameService::joinGame));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Route genericHandler(Function<ServiceMessage, ServiceMessage> func) {
        return (req, res) -> {
            var result = func.apply(getBody(req));
            res.status(result.statusCode());
            return makeBody(result);
        };
    }

    private static ServiceMessage getBody(Request req) {
        var obj = new Gson().fromJson(req.body(), ServiceMessage.class);
        if (obj == null) {
            obj = ServiceMessage.builder().build();
        }
        return ServiceMessage.builder()
                .setAuthToken(req.headers("Authorization"))
                .setUsername(obj.username())
                .setPassword(obj.password())
                .setEmail(obj.email())
                .setMessage(obj.message())
                .setStatusCode(obj.statusCode())
                .build();
    }

    private String makeBody(ServiceMessage clump) {
        return new Gson().toJson(clump);
    }
}
