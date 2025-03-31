package ru.mrrex.estranslator.dictionary.keyword;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import ru.mrrex.estranslator.dictionary.AbstractDictionary;

public class KeywordDictionary extends AbstractDictionary<String, String> {

    public KeywordDictionary() {
        super();
    }

    public KeywordDictionary(Map<String, String> map) {
        super(map);
    }

    public String setKeyword(String keyword, String value) {
        return dictionary.put(keyword, value);
    }

    public void addKeyword(String keyword, String value) {
        if (dictionary.containsKey(keyword)) {
            String message = "Keyword \"%s\" is already present in the dictionary".formatted(keyword);
            throw new IllegalArgumentException(message);
        }

        setKeyword(keyword, value);
    }

    public Set<String> getKeywords() {
        return Set.copyOf(dictionary.keySet());
    }

    public String getValue(String keyword) {
        return dictionary.get(keyword);
    }

    public KeywordDictionary intersection(KeywordDictionary otherDictionary) {
        KeywordDictionary jointDictionary = new KeywordDictionary();

        dictionary.forEach((keyword, value) -> {
            if (!dictionary.containsValue(value) || !otherDictionary.dictionary.containsValue(value))
                return;

            jointDictionary.addKeyword(keyword, value);
        });

        return jointDictionary;
    }

    public KeywordDictionary reverse() {
        Map<String, String> reversedMap = new LinkedHashMap<>();

        dictionary.forEach((k, v) -> {
            if (!reversedMap.containsKey(v))
                reversedMap.put(v, k);
        });

        return new KeywordDictionary(reversedMap);
    }

    @Override
    public String toString() {
        return "KeywordDictionary [keywords=" + size() + "]";
    }
}
