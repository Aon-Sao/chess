package server;

import io.javalin.Javalin;
import service.ServiceHelpers;

import static service.ServiceHelpers.authWrapper;
import static service.ServiceHelpers.exceptionWrapper;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db",      exceptionWrapper(ServiceHelpers::clearAll))
                .post(  "/user",    exceptionWrapper(UserService::register))
                .post(  "/session", exceptionWrapper(UserService::login))
                .delete("/session", authWrapper(exceptionWrapper(UserService::logout)))
                .get(   "/game",    authWrapper(exceptionWrapper(GameService::listGames)))
                .post(  "/game",    authWrapper(exceptionWrapper(GameService::createGame)))
                .put(   "/game",    authWrapper(exceptionWrapper(GameService::joinGame)));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
