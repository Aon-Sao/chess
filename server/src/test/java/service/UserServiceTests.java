package service;

import dataaccess.AuthDataAcc;
import dataaccess.UserDataAcc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTests {
    @Test
    public void registerUser() {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        var userData = UserDataAcc.getInstance();
        var authData = AuthDataAcc.getInstance();
        var result = UserService.register(request);
        Assertions.assertEquals(200, result.statusCode());
        Assertions.assertTrue(userData.hasUser(request.username()));
        Assertions.assertTrue(authData.hasAuth(result.authToken()));
        Assertions.assertTrue(authData.hasUser(result.username()));
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
    public void loginCorrect() {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        UserService.register(request);
        var result = UserService.login(request);
        var authData = AuthDataAcc.getInstance();
        Assertions.assertEquals(200, result.statusCode());
        Assertions.assertTrue(authData.hasAuth(result.authToken()));
        Assertions.assertTrue(authData.hasUser(result.username()));
    }

    @Test
    public void loginIncorrect() {
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        UserService.register(request);
        var loginRequest = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("wrong-password")
                .build();
        var result = UserService.login(loginRequest);
        var authData = AuthDataAcc.getInstance();
        Assertions.assertEquals(401, result.statusCode());
        Assertions.assertFalse(authData.hasAuth(result.authToken()));
        Assertions.assertFalse(authData.hasUser(result.username()));
    }
}
