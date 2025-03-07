package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.*;

public class GameDataAcc implements GameDAO {
    // Singleton
    private static GameDataAcc instance = null;
    private GameDataAcc() {
        clearAll();
    }
    public static GameDataAcc getInstance() {
        if (instance == null) {
            instance = new GameDataAcc();
        }
        return instance;
    }

    private Map<String, GameDataRec> games;

    @Override
    public String createGame(GameDataRec gameData) {
        var id = UUID.randomUUID().toString();
        games.put(id, gameData);
        return id;
    }

    @Override
    public GameDataRec getGame(String id) {
        return games.get(id);
    }

    @Override
    public Collection<GameDataRec> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void changeGameID(String id, int gameID) {
        var game = getGame(id);
        game = game.changeGameID(gameID);
        games.put(id, game);
    }

    @Override
    public void changeWhiteUsername(String id, String username) {
        var game = getGame(id);
        game = game.changeWhiteUsername(username);
        games.put(id, game);
    }

    @Override
    public void changeBlackUsername(String id, String username) {
        var game = getGame(id);
        game = game.changeBlackUsername(username);
        games.put(id, game);
    }

    @Override
    public void changeGameName(String id, String name) {
        var game = getGame(id);
        game = game.changeGameName(name);
        games.put(id, game);
    }

    @Override
    public void changeGameObj(String id, ChessGame gameObj) {
        var game = getGame(id);
        game = game.changeGameObj(gameObj);
        games.put(id, game);
    }

    @Override
    public void clearAll() {
        games = new HashMap<String, GameDataRec>();
    }

    @Override
    public void clearGame(String id) {
        games.remove(id);
    }
}
