package service;

import dataaccess.AuthDAOMem;
import dataaccess.DataAccessException;
import dataaccess.GameDAOMem;
import dataaccess.UserDAOMem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTests {

    @BeforeEach
    public void initEach() {
        // Make sure all data stores are clean
        new AuthDAOMem().clearAll();
        new GameDAOMem().clearAll();
        new UserDAOMem().clearAll();
    }

    @Test
    public void createGame200() throws DataAccessException {
        var loginResult = UserService.register(ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build()
        );
        var authToken = loginResult.authToken();
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        var gameData = new GameDAOMem();
        Assertions.assertEquals(200, createResult.statusCode());
        Assertions.assertEquals(1, gameData.listGames().size());
    }

    @Test
    public void createGameBadAuth() throws DataAccessException {
        var authToken = "badAuthToken";
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        var gameData = new GameDAOMem();
        Assertions.assertEquals(401, createResult.statusCode());
        Assertions.assertTrue(gameData.listGames().isEmpty());
    }

    @Test
    public void listGames200() throws DataAccessException {
        var loginResult = UserService.register(ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build()
        );
        var authToken = loginResult.authToken();
        var gameData = new GameDAOMem();
        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertEquals(200, listResult.statusCode());
        Assertions.assertEquals(0, gameData.listGames().size());

        GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertEquals(200, listResult.statusCode());
        Assertions.assertEquals(1, gameData.listGames().size());

        GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertEquals(200, listResult.statusCode());
        Assertions.assertEquals(2, gameData.listGames().size());
    }

    @Test
    public void listGamesBadAuth() throws DataAccessException {
        var authToken = "badAuthToken";
        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        Assertions.assertEquals(401, listResult.statusCode());
        Assertions.assertTrue(listResult.games().isEmpty());
    }

    @Test
    public void joinGame200() throws DataAccessException {
        var loginResult = UserService.register(ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build()
        );
        var authToken = loginResult.authToken();
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        int gameID = createResult.gameID();
        var joinResult = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameID(gameID)
                .setPlayerColor("WHITE")
                .build());
        Assertions.assertEquals(200, joinResult.statusCode());

        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertTrue(listResult.games().stream().toList().getFirst().whiteUsername().contains("test-username"));
        Assertions.assertNull(listResult.games().stream().toList().getFirst().blackUsername());
    }

    @Test
    public void joinGame400() throws DataAccessException {
        var loginResult = UserService.register(ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build()
        );
        var authToken = loginResult.authToken();
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        int gameID = createResult.gameID();
        var joinResult = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameID(gameID)
                .setPlayerColor("BLUE")
                .build());
        Assertions.assertEquals(400, joinResult.statusCode());

        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertNull(listResult.games().stream().toList().getFirst().whiteUsername());
        Assertions.assertNull(listResult.games().stream().toList().getFirst().blackUsername());
    }

    @Test
    public void joinGame403() throws DataAccessException {
        var loginResult = UserService.register(ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build()
        );
        var authToken = loginResult.authToken();
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        int gameID = createResult.gameID();
        GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameID(gameID)
                .setPlayerColor("WHITE")
                .build());
        var joinResult = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameID(gameID)
                .setPlayerColor("WHITE")
                .build());
        Assertions.assertEquals(403, joinResult.statusCode());

        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertTrue(listResult.games().stream().toList().getFirst().whiteUsername().contains("test-username"));
        Assertions.assertNull(listResult.games().stream().toList().getFirst().blackUsername());
    }
}
