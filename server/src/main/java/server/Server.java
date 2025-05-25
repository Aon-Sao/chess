package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.*;
import service.*;

import java.util.Map;
import java.util.function.Function;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        // No auth needed
        Spark.delete("/db",      genericHandler(ServiceHelpers::clearAll));
        Spark.post(  "/user",    genericHandler(UserService::register));
        Spark.post(  "/session", genericHandler(UserService::login));
        // Auth needed
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

    private Route genericHandler(LambdasCanThrow<ServiceMessage, ServiceMessage> func) {
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
                .setPlayerColor(obj.playerColor())
                .setGameName(obj.gameName())
                .setGameID(obj.gameID())
                .setGames(obj.games())
                .build();
    }

    private String makeBody(ServiceMessage msg) {
        var jsonObj = new JsonObject();
        var jsonElem = JsonParser.parseString(new Gson().toJson(msg.games()));
        jsonObj.add("games", jsonElem);
        var jsonObj2 = new Gson().toJsonTree(msg.toMap()).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
            jsonObj2.add(entry.getKey(), entry.getValue());
        }
        return jsonObj2.toString();
    }

}
