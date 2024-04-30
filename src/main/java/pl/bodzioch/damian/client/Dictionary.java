package pl.bodzioch.damian.client;

import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {

    private static final ConcurrentHashMap<DictionaryKey, String> dictionary = new ConcurrentHashMap<>();

    public enum DictionaryKey {
        BUR
    }

    public static void put(DictionaryKey key, String value) {
        dictionary.put(key, value);
    }

    public static String get(DictionaryKey key) {
        return dictionary.get(key);
    }
}
