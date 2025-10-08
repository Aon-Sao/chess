package server;

import io.javalin.Javalin;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
//                .delete("/db", ServiceHelpers::clearAll)
                .post(  "/user",     UserService::registerHandler)
                .post(  "/session",  UserService::loginHandler)
                .delete("/session",  UserService::logoutHandler)
//                .get(   "/game",     GameService::listGames)
                .post(  "/game",     GameService::createGameHandler);
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
