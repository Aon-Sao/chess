package model;

public record AuthDataRec(String authToken, String username) {
    public AuthDataRec changeUsername(String username) {
        return new AuthDataRec(authToken, username);
    }

    public AuthDataRec changeToken(String authToken) {
        return new AuthDataRec(authToken, username);
    }
}
