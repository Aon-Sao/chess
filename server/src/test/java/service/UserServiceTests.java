package service;

import dataaccess.AuthDAOMem;
import dataaccess.DataAccessException;
import dataaccess.GameDAOMem;
import dataaccess.UserDAOMem;
import model.AuthDataRec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {

    @BeforeEach
    public void initEach() {
        // Make sure all data stores are clean
        new AuthDAOMem().clearAll();
        new GameDAOMem().clearAll();
        new UserDAOMem().clearAll();
    }

    @Test
    public void registerUser() {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        var userData = new UserDAOMem();
        var authData = new AuthDAOMem();
        var result = UserService.register(request);
        Assertions.assertEquals(200, result.statusCode());

        Assertions.assertEquals(1, userData.listUsers().size());
        Assertions.assertEquals(request.username(), userData.listUsers().stream().findFirst().get().username());

        Assertions.assertEquals(1, authData.listAuths().size());
        Assertions.assertEquals(request.username(), authData.listAuths().stream().findFirst().get().username());
        Assertions.assertEquals(result.authToken(), authData.listAuths().stream().findFirst().get().authToken());
    }

    @Test
    public void registerDuplicateUser() {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        UserService.register(request);
        var result = UserService.register(request);
        Assertions.assertEquals(403, result.statusCode());
    }

    @Test
    public void registerNeedsEmail() {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .build();
        var result = UserService.register(request);
        Assertions.assertEquals(400, result.statusCode());
    }

    @Test
    public void loginCorrect() throws DataAccessException {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        var result = UserService.register(request);
        result = UserService.logout(ServiceMessage.builder().setAuthToken(result.authToken()).build());
        result = UserService.login(request);
        var authData = new AuthDAOMem();
        Assertions.assertEquals(200, result.statusCode());
        Assertions.assertEquals(1, authData.listAuths().size());
        Assertions.assertEquals(request.username(), authData.listAuths().stream().findFirst().get().username());
        Assertions.assertEquals(result.authToken(), authData.listAuths().stream().findFirst().get().authToken());
    }

    @Test
    public void loginIncorrect() throws DataAccessException {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        var result = UserService.register(request);
        result = UserService.logout(ServiceMessage.builder().setAuthToken(result.authToken()).build());
        var loginRequest = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("wrong-password")
                .build();
        result = UserService.login(loginRequest);
        var authData = new AuthDAOMem();
        Assertions.assertEquals(401, result.statusCode());
        Assertions.assertEquals(0, authData.listAuths().size());
    }

    @Test
    public void logoutExistingSession() throws DataAccessException {
        // Start with clear db
        ServiceHelpers.clearAll();
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        var result = UserService.register(request);
        UserService.logout(ServiceMessage.builder()
                .setAuthToken(result.authToken())
                .build());
        var authData = new AuthDAOMem();
        Assertions.assertTrue(authData.listAuths().isEmpty());
    }

    @Test
    public void logoutBadToken() throws DataAccessException {
        var result = UserService.logout(ServiceMessage.builder()
                .setAuthToken("bad-auth-token")
                .build());
        Assertions.assertEquals(401, result.statusCode());
    }
}
