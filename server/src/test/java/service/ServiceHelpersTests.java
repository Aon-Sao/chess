package service;

import dataaccess.AuthDAOMem;
import dataaccess.DataAccessException;
import dataaccess.GameDAOMem;
import dataaccess.UserDAOMem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceHelpersTests {

    @BeforeEach
    public void initEach() {
        // Make sure all data stores are clean
        new AuthDAOMem().clearAll();
        new GameDAOMem().clearAll();
        new UserDAOMem().clearAll();
    }

    @Test
    public void testClearAll() throws DataAccessException {
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
        Assertions.assertTrue(new UserDAOMem().listUsers().isEmpty());
        Assertions.assertTrue(new AuthDAOMem().listAuths().isEmpty());
        Assertions.assertTrue(new GameDAOMem().listGames().isEmpty());
    }
}
