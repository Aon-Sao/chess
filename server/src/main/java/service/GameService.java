package service;

import chess.ChessGame;
import dataaccess.GameDataAcc;
import model.GameDataRec;

import java.time.Instant;

import static service.UserService.isAuthorized;

public class GameService {
    private static int makeGameID(String gameName) {
        return Integer.parseInt(gameName.hashCode() + "" + Instant.now().getEpochSecond());
    }

    private static ServiceMessage createGame(ServiceMessage request) {
        if (isAuthorized(request)) {
            // Action
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
            // Response
            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .setGameID(id)
                    .build();
        } else {
            return ServiceMessage.builder()
                    .setStatusCode(401)
                    .setMessage("Error: unauthorized")
                    .build();
        }
    }
}
