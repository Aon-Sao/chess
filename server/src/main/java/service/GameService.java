package service;

import chess.ChessGame;
import dataaccess.GameDataAcc;
import model.GameDataRec;

import java.time.Instant;

import static service.ServiceHelpers.isAuthorized;

public class GameService {
    private static int makeGameID(String gameName) {
        return Integer.parseInt(gameName.hashCode() + "" + Instant.now().getEpochSecond());
    }

    public static void clear() {
        GameDataAcc.getInstance().clearAll();
    }

    public static ServiceMessage createGame(ServiceMessage request) {
            var name = request.gameName();
            if (name.isEmpty()) {
                return ServiceMessage.builder()
                        .setStatusCode(400)
                        .setMessage("Error: bad request")
                        .build();
            }
            var id = makeGameID(name);
            var gameRec = new GameDataRec(id,
                    null,
                    null,
                    name,
                    new ChessGame());
            var gameData = GameDataAcc.getInstance();
            gameData.createGame(gameRec);
            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .setGameID(id)
                    .build();
    }

    public static ServiceMessage listGames(ServiceMessage request) { return null; }
    public static ServiceMessage joinGame(ServiceMessage request) { return null; }
}
