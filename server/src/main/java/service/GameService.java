package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.GameDataAcc;
import model.GameDataRec;

import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static service.ServiceHelpers.isAuthorized;

public class GameService {
    private static int makeGameID(String gameName) {
        int newID = 0;
        var games = GameDataAcc.getInstance().listGames();
        HashSet<Integer> usedIDs = new HashSet<>(games.stream().map(GameDataRec::gameID).toList());
        while (usedIDs.contains(newID) && (newID != -1)) {
            newID++;
        }
        if (newID == -1) {
            throw new RuntimeException("Out of unique integer gameIDs");
        }
        return newID;
    }

    public static void clear() {
        GameDataAcc.getInstance().clearAll();
    }

    public static ServiceMessage createGame(ServiceMessage request) {
            var name = request.gameName();
            if (name.isEmpty()) {
                return ServiceHelpers.StockResponses.BAD_REQUEST.value();
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

    public static ServiceMessage listGames(ServiceMessage request) {
        var games = GameDataAcc.getInstance().listGames();
        games = new ArrayList<>(games.stream().map(GameDataRec::copy).toList()); // Copy
        // Strip game instance from records to avoid including the board in the response
        // Game board state should not be exposed by this method
        games = new ArrayList<>(games.stream().map((g) -> g.changeGameObj(null)).toList());
        // Ensure usernames serialize when null
        games = new ArrayList<>(games.stream().map((g) -> g.changeWhiteUsername(g.whiteUsername() + "")).toList());
        games = new ArrayList<>(games.stream().map((g) -> g.changeBlackUsername(g.blackUsername() + "")).toList());

        return ServiceMessage.builder()
                .setStatusCode(200)
                .setGames(games)
                .build();
    }

    public static ServiceMessage joinGame(ServiceMessage request) {
        String username = ServiceHelpers.getUsernameByAuthToken(request.authToken());
        var gameData = GameDataAcc.getInstance();
        String gameUUID = gameData.findGameByGameID(request.gameID());
        var game = gameData.getGame(gameUUID);

        if ((game == null) || (!(List.of("BLACK", "WHITE").contains(request.playerColor())))) {
            return ServiceHelpers.StockResponses.BAD_REQUEST.value();
        }
        if (((request.playerColor().equals("WHITE")) && (game.whiteUsername() != null))
            || ((request.playerColor().equals("BLACK")) && (game.blackUsername() != null))) {
            return ServiceHelpers.StockResponses.ALREADY_TAKEN.value();
        }

        if (request.playerColor().equals("WHITE")) {
            gameData.changeWhiteUsername(gameUUID, username);
        } else {
            gameData.changeBlackUsername(gameUUID, username);
        }

        return ServiceMessage.builder()
                .setStatusCode(200)
                .build();
    }
}
