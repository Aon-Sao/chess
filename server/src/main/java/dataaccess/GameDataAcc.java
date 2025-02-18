package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.*;

public class GameDataAcc implements GameDAO {
    // Singleton
    private GameDataAcc instance = null;
    private GameDataAcc() {
        clearAll();
    }
    public GameDataAcc getInstance() {
        if (instance == null) {
            instance = new GameDataAcc();
        }
        return instance;
    }

    private Map<UUID, GameDataRec> games;

    @Override
    public void createGame(GameDataRec gameData) {
        var id = UUID.randomUUID();
        games.put(id, gameData);
    }

    @Override
    public GameDataRec getGame(UUID id) {
        return games.get(id);
    }

    @Override
    public Collection<GameDataRec> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void changeGameID(UUID id, int gameID) {
        var game = getGame(id);
        game = game.changeGameID(gameID);
        games.put(id, game);
    }

    @Override
    public void changeWhiteUsername(UUID id, String username) {
        var game = getGame(id);
        game = game.changeWhiteUsername(username);
        games.put(id, game);
    }

    @Override
    public void changeBlackUsername(UUID id, String username) {
        var game = getGame(id);
        game = game.changeBlackUsername(username);
        games.put(id, game);
    }

    @Override
    public void changeGameName(UUID id, String name) {
        var game = getGame(id);
        game = game.changeGameName(name);
        games.put(id, game);
    }

    @Override
    public void changeGameObj(UUID id, ChessGame gameObj) {
        var game = getGame(id);
        game = game.changeGameObj(gameObj);
        games.put(id, game);
    }

    @Override
    public void clearAll() {
        games = new HashMap<>();
    }

    @Override
    public void clearGame(UUID id) {
        games.remove(id);
    }
}
