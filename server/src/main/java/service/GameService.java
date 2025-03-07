package service;

import static service.UserService.isAuthorized;

public class GameService {
    private static ServiceMessage createGame(ServiceMessage request) {
        if (isAuthorized(request)) {
            // Act
            // TODO: merge and rename UserClump and GameClump (the latter is unwritten)
        } else {
            return ServiceMessage.builder()
                    .setStatusCode(401)
                    .setMessage("Error: unauthorized")
                    .build();
        }
    }
}
