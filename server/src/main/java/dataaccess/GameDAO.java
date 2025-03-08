package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.Collection;

public interface GameDAO {
    // Create
    void createGame(GameDataRec gameData);

    // Read
    GameDataRec getGame(String id);
    Collection<GameDataRec> listGames();

    // Update
    void changeGameID(String id, int gameID);
    void changeWhiteUsername(String id, String username);
    void changeBlackUsername(String id, String username);
    void changeGameName(String id, String gameName);
    void changeGameObj(String id, ChessGame game);

    // Delete
    void clearAll();
    void clearGame(String id);


}
