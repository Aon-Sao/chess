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
    void changeWhiteUsername(String id, String username);
    void changeBlackUsername(String id, String username);

    // Delete
    void clearAll();


}
