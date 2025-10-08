package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

public record AuthDataRec(String authToken, String username) {
    public JsonObject toJson() {
        return (JsonObject) new Gson().toJsonTree(this);
    }
    public static AuthDataRec fromJson(JsonObject json) {
        return new Gson().fromJson(json.getAsString(), AuthDataRec.class);
    }

    public AuthDataRec fromMap(Map<String, Object> m) {
        return new AuthDataRec((String) m.get("authToken"), (String) m.get("username"));
    }
}
