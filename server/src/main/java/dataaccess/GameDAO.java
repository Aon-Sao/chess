package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.ArrayList;

public interface GameDAO {
    void createGame(GameDataRec data) throws DataAccessException;
    GameDataRec getGame(int gameID) throws DataAccessException;
    ArrayList<GameDataRec> listGames() throws DataAccessException;
    void changeUsername(int gameID, ChessGame.TeamColor teamColor, String username) throws DataAccessException;
    void removeGame(int gameID) throws DataAccessException;
    void clearAll() throws DataAccessException;
}
