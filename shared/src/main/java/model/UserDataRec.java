package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

public record UserDataRec(String username, String password, String email) implements hasFromMap {
    public JsonObject toJson() {
        return (JsonObject) new Gson().toJsonTree(this);
    }
    public static GameDataRec fromJson(JsonObject json) {
        return new Gson().fromJson(json.getAsString(), GameDataRec.class);
    }

    public UserDataRec fromMap(Map<String, Object> m) {
        return new UserDataRec((String) m.get("username"), (String) m.get("password"), (String) m.get("email"));
    }
}
