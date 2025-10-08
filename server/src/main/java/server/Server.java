package server;

import io.javalin.Javalin;
import service.ServiceHelpers;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
//                .delete("/db", ServiceHelpers::clearAll)
                .post(  "/user",     UserService::register)
                .post(  "/session",  UserService::login);
//                .delete("/session",  UserService::logout)
//                .get(   "/game",     GameService::listGames)
//                .post(  "/game",     GameService::createGame)
//                .put(   "/game",     GameService::joinGame);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
