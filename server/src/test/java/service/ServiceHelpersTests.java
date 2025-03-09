package service;

import dataaccess.AuthDataAcc;
import dataaccess.GameDataAcc;
import dataaccess.UserDataAcc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceHelpersTests {

    @Test
    public void testClearAll() {
        var userData = UserDataAcc.getInstance();
        var authData = AuthDataAcc.getInstance();
        var gameData = GameDataAcc.getInstance();

        // Create a user
        UserService.register(ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build());

        // Create a game
        GameService.createGame(ServiceMessage.builder()
                .setGameName("game-name")
                .build());

        // Clear
        ServiceHelpers.clearAll();

        // Should be empty
        Assertions.assertTrue(userData.listUsers().isEmpty());
        Assertions.assertTrue(authData.listAuths().isEmpty());
        Assertions.assertTrue(gameData.listGames().isEmpty());
    }
}
