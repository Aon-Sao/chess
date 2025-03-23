package model;

import java.util.Map;

public interface hasFromMap {
    public default Object fromMap(Map<String, Object> m) {
        return null;
    }
}
