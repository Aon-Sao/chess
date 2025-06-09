package service;

import dataaccess.*;
import model.AuthDataRec;
import model.UserDataRec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {

    @BeforeEach
    public void initEach() throws DataAccessException {
        // Make sure all data stores are clean
        new AuthDAOMem().clearAll();
        new GameDAOMem().clearAll();
        new UserDAODB().clearAll();
    }

    @Test
    public void registerUserCorrectly() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var response1 = UserService.register(request1);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(1, userData.listUsers().size());
        Assertions.assertTrue(userData.listUsers().contains(user1));
        Assertions.assertTrue(authData.listAuths().contains(auth1));
    }

    @Test
    public void registerDuplicateUsername() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var user2 = new UserDataRec(
                "test-username-1",
                "test-password-2",
                "test-email-2");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var request2 = ServiceMessage.builder()
                .setUsername(user2.username())
                .setPassword(user2.password())
                .setEmail(user2.email())
                .build();
        var response1 = UserService.register(request1);
        var response2 = UserService.register(request2);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        var auth2 = new AuthDataRec(response2.authToken(), user2.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(403, response2.statusCode());
        Assertions.assertEquals(1, userData.listUsers().size());
        Assertions.assertEquals(1, authData.listAuths().size());
        Assertions.assertTrue(userData.listUsers().contains(user1));
        Assertions.assertFalse(userData.listUsers().contains(user2));
        Assertions.assertTrue(authData.listAuths().contains(auth1));
        Assertions.assertFalse(authData.listAuths().contains(auth2));
    }

    @Test
    public void registerDuplicatePassword() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var user2 = new UserDataRec(
                "test-username-2",
                "test-password-1",
                "test-email-2");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var request2 = ServiceMessage.builder()
                .setUsername(user2.username())
                .setPassword(user2.password())
                .setEmail(user2.email())
                .build();
        var response1 = UserService.register(request1);
        var response2 = UserService.register(request2);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        var auth2 = new AuthDataRec(response2.authToken(), user2.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertEquals(2, userData.listUsers().size());
        Assertions.assertEquals(2, authData.listAuths().size());
        Assertions.assertTrue(userData.listUsers().contains(user1));
        Assertions.assertTrue(userData.listUsers().contains(user2));
        Assertions.assertTrue(authData.listAuths().contains(auth1));
        Assertions.assertTrue(authData.listAuths().contains(auth2));
    }

    @Test
    public void registerDuplicateEmail() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var user2 = new UserDataRec(
                "test-username-2",
                "test-password-2",
                "test-email-1");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var request2 = ServiceMessage.builder()
                .setUsername(user2.username())
                .setPassword(user2.password())
                .setEmail(user2.email())
                .build();
        var response1 = UserService.register(request1);
        var response2 = UserService.register(request2);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        var auth2 = new AuthDataRec(response2.authToken(), user2.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(403, response2.statusCode());
        Assertions.assertEquals(1, userData.listUsers().size());
        Assertions.assertEquals(1, authData.listAuths().size());
        Assertions.assertTrue(userData.listUsers().contains(user1));
        Assertions.assertFalse(userData.listUsers().contains(user2));
        Assertions.assertTrue(authData.listAuths().contains(auth1));
        Assertions.assertFalse(authData.listAuths().contains(auth2));
    }

    @Test
    public void registerNeedsUsername() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var request = ServiceMessage.builder()
                .setPassword("test-password")
                .setEmail("test-email")
                .build();
        var result = UserService.register(request);
        Assertions.assertEquals(400, result.statusCode());
        Assertions.assertTrue(userData.listUsers().isEmpty());
        Assertions.assertTrue(authData.listAuths().isEmpty());
    }

    @Test
    public void registerNeedsPassword() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setEmail("test-email")
                .build();
        var result = UserService.register(request);
        Assertions.assertEquals(400, result.statusCode());
        Assertions.assertTrue(userData.listUsers().isEmpty());
        Assertions.assertTrue(authData.listAuths().isEmpty());
    }

    @Test
    public void registerNeedsEmail() throws DataAccessException {
        var userData = new UserDAODB();
        var authData = new AuthDAOMem();
        var request = ServiceMessage.builder()
                .setUsername("test-username")
                .setPassword("test-password")
                .build();
        var result = UserService.register(request);
        Assertions.assertEquals(400, result.statusCode());
        Assertions.assertTrue(userData.listUsers().isEmpty());
        Assertions.assertTrue(authData.listAuths().isEmpty());
    }

    @Test
    public void loginCorrect() throws DataAccessException {
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var response1 = UserService.register(request1);
        var request2 = ServiceMessage.builder()
                .setAuthToken(response1.authToken())
                .build();
        var response2 = UserService.logout(request2);
        var request3 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .build();
        var response3 = UserService.login(request3);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        var auth2 = new AuthDataRec(response3.authToken(), user1.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertEquals(200, response3.statusCode());
        Assertions.assertEquals(1, authData.listAuths().size());
        Assertions.assertFalse(authData.listAuths().contains(auth1));
        Assertions.assertTrue(authData.listAuths().contains(auth2));
    }

    @Test
    public void loginBadPassword() throws DataAccessException {
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var response1 = UserService.register(request1);
        var request2 = ServiceMessage.builder()
                .setAuthToken(response1.authToken())
                .build();
        var response2 = UserService.logout(request2);
        var request3 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword("bad-password")
                .build();
        var response3 = UserService.login(request3);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        var auth2 = new AuthDataRec(response3.authToken(), user1.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertEquals(401, response3.statusCode());
        Assertions.assertTrue(authData.listAuths().isEmpty());
        Assertions.assertFalse(authData.listAuths().contains(auth1));
        Assertions.assertFalse(authData.listAuths().contains(auth2));
    }

    @Test
    public void loginNoUsername() throws DataAccessException {
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var response1 = UserService.register(request1);
        var request2 = ServiceMessage.builder()
                .setAuthToken(response1.authToken())
                .build();
        var response2 = UserService.logout(request2);
        var request3 = ServiceMessage.builder()
                .setPassword(user1.password())
                .build();
        var response3 = UserService.login(request3);
        var auth1 = new AuthDataRec(response1.authToken(), user1.username());
        var auth2 = new AuthDataRec(response3.authToken(), user1.username());
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertEquals(400, response3.statusCode());
        Assertions.assertTrue(authData.listAuths().isEmpty());
        Assertions.assertFalse(authData.listAuths().contains(auth1));
        Assertions.assertFalse(authData.listAuths().contains(auth2));
    }

    @Test
    public void logoutExistingSession() throws DataAccessException {
        var authData = new AuthDAOMem();
        var user1 = new UserDataRec(
                "test-username-1",
                "test-password-1",
                "test-email-1");
        var request1 = ServiceMessage.builder()
                .setUsername(user1.username())
                .setPassword(user1.password())
                .setEmail(user1.email())
                .build();
        var response1 = UserService.register(request1);
        var request2 = ServiceMessage.builder()
                .setAuthToken(response1.authToken())
                .build();
        var response2 = UserService.logout(request2);
        Assertions.assertEquals(200, response1.statusCode());
        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertTrue(authData.listAuths().isEmpty());
    }

    @Test
    public void logoutBadToken() throws DataAccessException {
        var result = UserService.logout(ServiceMessage.builder()
                .setAuthToken("bad-auth-token")
                .build());
        Assertions.assertEquals(401, result.statusCode());
    }

    @Test
    // Uncertain whether to return 400 or 401
    public void logoutMissingToken() throws DataAccessException {
        var result = UserService.logout(ServiceMessage.builder()
                .build());
        Assertions.assertEquals(401, result.statusCode());
    }
}
