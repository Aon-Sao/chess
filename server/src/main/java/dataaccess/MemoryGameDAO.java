package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private static List<GameDataRec> games = new ArrayList<>();

    private static GameDataRec findGame(int gameID) throws DataAccessException {
        for (var game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("No such gameID");
    }

    public MemoryGameDAO() {

    }

    @Override
    public void createGame(GameDataRec data) {
        games.add(data);
    }

    @Override
    public GameDataRec getGame(int gameID) throws DataAccessException {
        return findGame(gameID);
    }

    @Override
    public ArrayList<GameDataRec> listGames() {
        return new ArrayList<>(games);
    }

    @Override
    public void changeUsername(int gameID, ChessGame.TeamColor teamColor, String username) throws DataAccessException {
        var game = findGame(gameID);
        games.remove(game);
        if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
            game = game.changeWhiteUsername(username);
        } else {
            game = game.changeBlackUsername(username);
        }
        games.add(game);
    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {
        games.remove(findGame(gameID));
    }

    @Override
    public void clearAll() {
        games = new ArrayList<>();
    }
}
