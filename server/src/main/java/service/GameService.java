package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAOMem;
import model.GameDataRec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class GameService {
    private static int makeGameID() {
        int newID = 1;
        var games = new GameDAOMem().listGames();
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
        new GameDAOMem().clearAll();
    }

    public static ServiceMessage createGame(ServiceMessage msg) throws DataAccessException {
        return ServiceHelpers.authWrapper((request) -> {
            var name = request.gameName();
            if (name == null || name.isEmpty()) {
                return ServiceHelpers.StockResponses.BAD_REQUEST.value();
            }
            var id = makeGameID();
            var gameRec = new GameDataRec(id,
                    null,
                    null,
                    name,
                    new ChessGame());
            new GameDAOMem().createGame(gameRec);
            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .setGameID(id)
                    .build();
        }).apply((msg));
    }

    public static ServiceMessage listGames(ServiceMessage msg) throws DataAccessException {
        return ServiceHelpers.authWrapper((request) -> {
            var games = new GameDAOMem().listGames();
            games = new ArrayList<>(games.stream().map(GameDataRec::copy).toList()); // Copy
            // Strip game instance from records to avoid including the board in the response
            // Game board state should not be exposed by this method
            games = new ArrayList<>(games.stream().map((g) -> g.changeGameObj(null)).toList());

            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .setGames(games)
                    .build();
        }).apply(msg);
    }

    public static ServiceMessage joinGame(ServiceMessage request) throws DataAccessException {
        return ServiceHelpers.authWrapper((msg) -> {
            String username = ServiceHelpers.getUsernameByAuthToken(request.authToken());
            var gameData = new GameDAOMem();

            // There's probably a better way, I think it is called Optional
            GameDataRec game;
            try {
                game = gameData.getGame(request.gameID());
            } catch (DataAccessException e) {
                game = null;
            }

            if ((request.gameID() == 0)
                    || (game == null)
                    || request.playerColor() == null
                    || (!(List.of("BLACK", "WHITE").contains(request.playerColor())))) {
                return ServiceHelpers.StockResponses.BAD_REQUEST.value();
            }
            if (((request.playerColor().equals("WHITE")) && (game.whiteUsername() != null))
                    || ((request.playerColor().equals("BLACK")) && (game.blackUsername() != null))) {
                return ServiceHelpers.StockResponses.ALREADY_TAKEN.value();
            }

            gameData.changeUsername(request.gameID(), (Objects.equals(request.playerColor(), "WHITE") ? WHITE : BLACK) , username);

            return ServiceMessage.builder()
                    .setStatusCode(200)
                    .build();
        }).apply(request);
    }
}
