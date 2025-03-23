package service;


import model.GameDataRec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ServiceMessage {
    private final String username;
    private final String password;
    private final String email;
    private final String authToken;
    private final String message;
    private final String playerColor;
    private final String gameName;
    private final int gameID;
    private final int statusCode;
    private final Collection<GameDataRec> games;

    private ServiceMessage(SMBuilder SMBuilder) {
        this.username = SMBuilder.username;
        this.password = SMBuilder.password;
        this.email = SMBuilder.email;
        this.authToken = SMBuilder.authToken;
        this.message = SMBuilder.message;
        this.playerColor = SMBuilder.playerColor;
        this.gameName = SMBuilder.gameName;
        this.gameID = SMBuilder.gameID;
        this.statusCode = SMBuilder.statusCode;
        this.games = SMBuilder.games;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> m = new HashMap<>();
        if (!username.isEmpty()) {
            m.put("username", username);
        }
        if (!password.isEmpty()) {
            m.put("password", password);
        }
        if (!email.isEmpty()) {
            m.put("email", email);
        }
        if (!authToken.isEmpty()) {
            m.put("authToken", authToken);
        }
        if (!message.isEmpty()) {
            m.put("message", message);
        }
        if (!playerColor.isEmpty()) {
            m.put("playerColor", playerColor);
        }
        if (!gameName.isEmpty()) {
            m.put("gameName", gameName);
        }
        if (gameID != 0) {
            m.put("gameID", gameID + "");
        }
        if (statusCode != 0) {
            m.put("statusCode", statusCode + "");
        }
        if (!games.isEmpty()) {
            m.put("games", games.toString());
        }
//        m.put("games", games.toString());

        return m;
    }

    public static SMBuilder builder() {
        return new SMBuilder();
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String email() {
        return email;
    }

    public String authToken() {
        return authToken;
    }

    public String message() {
        return message;
    }

    public String playerColor() {
        return playerColor;
    }

    public String gameName() {
        return gameName;
    }

    public int gameID() {
        return gameID;
    }

    public int statusCode() {
        return statusCode;
    }

    public Collection<GameDataRec> games() {
        return games;
    }

    public static class SMBuilder {
        private String username = "";
        private String password = "";
        private String email = "";
        private String authToken = "";
        private String message = "";
        private String playerColor = "";
        private String gameName = "";
        private int gameID = 0;
        private int statusCode = 0;
        private Collection<GameDataRec> games = new ArrayList<>();

        public ServiceMessage build() {
            return new ServiceMessage(this);
        }

        public SMBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public SMBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public SMBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public SMBuilder setAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public SMBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SMBuilder setPlayerColor(String playerColor) {
            this.playerColor = playerColor;
            return this;
        }

        public SMBuilder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public SMBuilder setGameID(int gameID) {
            this.gameID = gameID;
            return this;
        }

        public SMBuilder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public SMBuilder setGames(Collection<GameDataRec> games) {
            this.games = games;
            return this;
        }
    }
}