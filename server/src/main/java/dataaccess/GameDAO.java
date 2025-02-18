package dataaccess;

import chess.ChessGame;
import model.GameDataRec;

import java.util.Collection;
import java.util.UUID;

public interface GameDAO {
    // Create
    void createGame(GameDataRec gameData);

    // Read
    GameDataRec getGame(UUID id);
    Collection<GameDataRec> listGames();

    // Update
    void changeGameID(UUID id, int gameID);
    void changeWhiteUsername(UUID id, String username);
    void changeBlackUsername(UUID id, String username);
    void changeGameName(UUID id, String gameName);
    void changeGameObj(UUID id, ChessGame game);

    // Delete
    void clearAll();
    void clearGame(UUID id);


}
