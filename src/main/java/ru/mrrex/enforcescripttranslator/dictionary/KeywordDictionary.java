package ru.mrrex.enforcescripttranslator.dictionary;

import java.util.HashMap;
import java.util.Map;
import ru.mrrex.enforcescripttranslator.exception.KeywordAlreadyExistsException;

public class KeywordDictionary {

    private final Map<String, String> dictionary;

    public KeywordDictionary() {
        this.dictionary = new HashMap<>();
    }

    public void addKeyword(String keyword, String value) {
        if (dictionary.containsKey(keyword))
            throw new KeywordAlreadyExistsException(keyword);

        dictionary.put(keyword, value);
    }

    public String getValue(String keyword) {
        return dictionary.get(keyword);
    }
}
