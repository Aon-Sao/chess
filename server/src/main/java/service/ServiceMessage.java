package service;


import model.GameDataRec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

    private ServiceMessage(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.authToken = builder.authToken;
        this.message = builder.message;
        this.playerColor = builder.playerColor;
        this.gameName = builder.gameName;
        this.gameID = builder.gameID;
        this.statusCode = builder.statusCode;
        this.games = builder.games;
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

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
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

        private boolean validate() {
            if (!(List.of("WHITE", "BLACK", "").contains(playerColor))) {
                return false;
            }

            if (!(List.of(0, 200, 400, 401, 403, 500).contains(statusCode))) {
                return false;
            }

            return true;
        }

        public ServiceMessage build() {
            return new ServiceMessage(this);
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPlayerColor(String playerColor) {
            this.playerColor = playerColor;
            return this;
        }

        public Builder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public Builder setGameID(int gameID) {
            this.gameID = gameID;
            return this;
        }

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setGames(Collection<GameDataRec> games) {
            this.games = games;
            return this;
        }
    }
}