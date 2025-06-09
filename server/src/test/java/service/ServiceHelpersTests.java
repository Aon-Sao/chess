package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceHelpersTests {

    @BeforeEach
    public void initEach() throws DataAccessException {
        // Make sure all data stores are clean
        new AuthDAODB().clearAll();
        new GameDAODB().clearAll();
        new UserDAODB().clearAll();
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
        Assertions.assertTrue(new UserDAODB().listUsers().isEmpty());
        Assertions.assertTrue(new AuthDAODB().listAuths().isEmpty());
        Assertions.assertTrue(new GameDAODB().listGames().isEmpty());
    }
}
