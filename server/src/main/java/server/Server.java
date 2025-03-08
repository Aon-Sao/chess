package server;

import com.google.gson.Gson;
import service.GameService;
import service.ServiceMessage;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
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

    private Object registerUser(Request req, Response res) {
        var result = UserService.register(getBody(req));
        res.status(result.statusCode());
        res.body(makeBody(result));
        return res.body();
    }

    private Object loginUser(Request req, Response res) {
        var result = UserService.login(getBody(req));
        res.status(result.statusCode());
        res.body(makeBody(result));
        return res.body();
    }

    private Object logoutUser(Request req, Response res) {
        var result = UserService.logout(getBody(req));
        res.status(result.statusCode());
        res.body(makeBody(result));
        return res.body();
    }

    private Object createGame(Request req, Response res) {
        var result = GameService.createGame(getBody(req));
        res.status(result.statusCode());
        res.body(makeBody(result));
        return res.body();
    }
}
