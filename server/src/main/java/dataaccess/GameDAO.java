package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.Collection;
import java.util.List;

public interface GameDAO {
    void createGame(GameDataRec data);
    GameDataRec getGame(int gameID) throws DataAccessException;
    Collection<GameDataRec> listGames();
    void changeUsername(int gameID, ChessGame.TeamColor teamColor, String username) throws DataAccessException;
    void removeGame(int gameID) throws DataAccessException;
    void clearAll();
}
