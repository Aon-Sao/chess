package model;

public record UserDataRec(String username, String password, String email) {
    public UserDataRec changeUsername(String username) {
        return new UserDataRec(username, password, email);
    }
    public UserDataRec changePassword(String password) {
        return new UserDataRec(username, password, email);
    }
    public UserDataRec changeEmail(String email) {
        return new UserDataRec(username, password, email);
    }

}
