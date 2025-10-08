package model;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

public record GameDataRec(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameDataRec changeWhiteUsername(String whiteUsername) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeBlackUsername(String blackUsername) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeGameObj(ChessGame game) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec copy() {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public JsonObject toJson() {
        return (JsonObject) new Gson().toJsonTree(this);
    }
    public static GameDataRec fromJson(JsonObject json) {
        return new Gson().fromJson(json.getAsString(), GameDataRec.class);
    }

    public GameDataRec fromMap(Map<String, Object> m) {
        return new GameDataRec(
                (int) m.get("gameID"),
                (String) m.get("whiteUsername"),
                (String) m.get("blackUsername"),
                (String) m.get("gameName"),
                new Gson().fromJson((String) m.get("game"), ChessGame.class));
    }
}
