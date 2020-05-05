package com.securenative;

import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static MapWrapper<String, String> defaultBuilder() {
        return new MapWrapper<>();
    }

    public static <K, V> MapWrapper<K, V> builder() {
        return new MapWrapper<K, V>();
    }

    public static final class MapWrapper<K, V> {
        private final HashMap<K, V> map;

        public MapWrapper() {
            map = new HashMap<K, V>();
        }

        public MapWrapper<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return map;
        }
    }
}
