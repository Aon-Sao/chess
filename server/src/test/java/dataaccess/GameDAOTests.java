package dataaccess;

import chess.ChessGame;
import model.GameDataRec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GameDAOTests {
    @Test
    @Order(1)
    public void effectivelyStatic() throws DataAccessException {
        // It is necessary for different instances of the GameDAO to agree on all public methods
        var instance1 = new GameDAOMem();
        var instance2 = new GameDAOMem();
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
        var game1 = new GameDataRec(0, "white", "black", "game1", new ChessGame());
        instance1.createGame(game1);
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
        Assertions.assertEquals(instance1.getGame(0), instance2.getGame(0));
        var game2 = new GameDataRec(1, "white", "black", "game2", new ChessGame());
        instance2.createGame(game2);
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
        Assertions.assertEquals(instance1.getGame(1), instance2.getGame(1));
        instance1.changeUsername(0, ChessGame.TeamColor.WHITE, "white0");
        Assertions.assertEquals(instance1.getGame(0), instance2.getGame(0));
        instance2.changeUsername(0, ChessGame.TeamColor.BLACK, "black0");
        Assertions.assertEquals(instance1.getGame(0), instance2.getGame(0));
        instance1.removeGame(0);
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
        instance2.removeGame(1);
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
        instance1.createGame(game1);
        instance2.createGame(game2);
        instance1.clearAll();
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
        instance1.createGame(game1);
        instance2.createGame(game2);
        instance2.clearAll();
        Assertions.assertEquals(instance1.listGames(), instance2.listGames());
    }
}
