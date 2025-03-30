package ru.mrrex.estranslator.cache;

import java.util.HashMap;
import java.util.Map;

public enum Cache {

    INSTANCE;

    private final Map<String, Object> map;

    private Cache() {
        this.map = new HashMap<>();
    }

    public Object get(String key) {
        return map.get(key);
    }

    public boolean hasKey(String key) {
        return map.containsKey(key);
    }

    public void set(String key, Object object) {
        map.put(key, object);
    }
}
