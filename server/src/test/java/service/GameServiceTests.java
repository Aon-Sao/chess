package service;

import dataaccess.*;
import model.AuthDataRec;
import model.UserDataRec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GameServiceTests {

    private static List<Object> makeUsers() throws DataAccessException {
        var user1 = new UserDataRec("test-user-1", "test-pass-1", "test-email-1");
        var user2 = new UserDataRec("test-user-2", "test-pass-2", "test-email-2");
        var auth1 = new AuthDataRec(UserService.register(
                ServiceMessage.builder()
                    .setUsername(user1.username())
                    .setPassword(user1.password())
                    .setEmail(user1.email())
                    .build()
                ).authToken(), user1.username());
        var auth2 = new AuthDataRec(UserService.register(
                ServiceMessage.builder().
                    setUsername(user2.username())
                    .setPassword(user2.password())
                    .setEmail(user2.email())
                    .build()
                ).authToken(), user2.username());
        return List.of(user1, auth1, user2, auth2);
    }

    @BeforeEach
    public void initEach() throws DataAccessException {
        // Make sure all data stores are clean
        new AuthDAOMem().clearAll();
        new GameDAOMem().clearAll();
        new UserDAODB().clearAll();
    }

    @Test
    public void createGame200() throws DataAccessException {
        var authToken = ((AuthDataRec) makeUsers().get(1)).authToken();
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
        var authToken = ((AuthDataRec) makeUsers().get(1)).authToken();
        authToken = "badAuthToken";
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        var gameData = new GameDAOMem();
        Assertions.assertEquals(401, createResult.statusCode());
        Assertions.assertTrue(gameData.listGames().isEmpty());
    }

    @Test
    public void createGameNoName() throws DataAccessException {
        var authToken = ((AuthDataRec) makeUsers().get(1)).authToken();
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        var gameData = new GameDAOMem();
        Assertions.assertEquals(400, createResult.statusCode());
        Assertions.assertTrue(gameData.listGames().isEmpty());
    }

    @Test
    public void listGames200() throws DataAccessException {
        var authToken = ((AuthDataRec) makeUsers().get(1)).authToken();
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
        var authToken = ((AuthDataRec) makeUsers().get(1)).authToken();
        authToken = "badAuthToken";
        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        Assertions.assertEquals(401, listResult.statusCode());
        Assertions.assertTrue(listResult.games().isEmpty());
    }

    @Test
    public void joinGameWhite() throws DataAccessException {
        var tmp = makeUsers();
        var userRec = (UserDataRec) tmp.get(0);
        var authToken = ((AuthDataRec) tmp.get(1)).authToken();
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
        Assertions.assertEquals(listResult.games().getFirst().whiteUsername(), userRec.username());
        Assertions.assertNull(listResult.games().getFirst().blackUsername());
    }

    @Test
    public void joinGameBlack() throws DataAccessException {
        var tmp = makeUsers();
        var userRec = (UserDataRec) tmp.get(0);
        var authToken = ((AuthDataRec) tmp.get(1)).authToken();
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameName("gameName")
                .build());
        int gameID = createResult.gameID();
        var joinResult = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authToken)
                .setGameID(gameID)
                .setPlayerColor("BLACK")
                .build());
        Assertions.assertEquals(200, joinResult.statusCode());

        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authToken)
                .build());
        Assertions.assertEquals(listResult.games().getFirst().blackUsername(), userRec.username());
        Assertions.assertNull(listResult.games().getFirst().whiteUsername());
    }

    @Test
    public void joinGame400() throws DataAccessException {
        var authToken = ((AuthDataRec) makeUsers().get(1)).authToken();
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
        Assertions.assertNull(listResult.games().getFirst().whiteUsername());
        Assertions.assertNull(listResult.games().getFirst().blackUsername());
    }

    @Test
    public void joinGame403() throws DataAccessException {
        var tmp = makeUsers();
        var userRec1 = (UserDataRec) tmp.get(0);
        var authRec1 = (AuthDataRec) tmp.get(1);
        var authRec2 = (AuthDataRec) tmp.get(3);
        var createResult = GameService.createGame(ServiceMessage.builder()
                .setAuthToken(authRec1.authToken())
                .setGameName("gameName")
                .build());
        int gameID = createResult.gameID();
        var response1 = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authRec1.authToken())
                .setGameID(gameID)
                .setPlayerColor("WHITE")
                .build());
        var response2 = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authRec1.authToken())
                .setGameID(gameID)
                .setPlayerColor("WHITE")
                .build());
        var response3 = GameService.joinGame(ServiceMessage.builder()
                .setAuthToken(authRec2.authToken())
                .setGameID(gameID)
                .setPlayerColor("WHITE")
                .build());
        var listResult = GameService.listGames(ServiceMessage.builder()
                .setAuthToken(authRec1.authToken())
                .build());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(403, response2.statusCode());
        Assertions.assertEquals(403, response3.statusCode());

        Assertions.assertEquals(listResult.games().getFirst().whiteUsername(), userRec1.username());
        Assertions.assertNull(listResult.games().getFirst().blackUsername());
    }
}
