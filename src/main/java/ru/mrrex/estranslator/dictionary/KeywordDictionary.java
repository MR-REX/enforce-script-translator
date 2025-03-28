package ru.mrrex.estranslator.dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import ru.mrrex.estranslator.exception.KeywordAlreadyExistsException;

public class KeywordDictionary {

    private final Map<String, String> dictionary;

    public KeywordDictionary() {
        this.dictionary = new HashMap<>();
    }

    public KeywordDictionary(KeywordDictionary dictionary) {
        this.dictionary = new HashMap<>(dictionary.dictionary);
    }

    public String setKeyword(String keyword, String value) {
        return dictionary.put(keyword, value);
    }

    public void addKeyword(String keyword, String value) {
        if (dictionary.containsKey(keyword))
            throw new KeywordAlreadyExistsException(keyword);

        setKeyword(keyword, value);
    }

    public Set<String> getKeywords() {
        Set<String> keywords = new HashSet<>();
        keywords.addAll(dictionary.keySet());

        return keywords;
    }

    public String getValue(String keyword) {
        return dictionary.get(keyword);
    }

    public void forEach(BiConsumer<String, String> consumer) {
        dictionary.forEach(consumer::accept);
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

    public int size() {
        return dictionary.size();
    }

    public boolean isEmpty() {
        return dictionary.isEmpty();
    }

    @Override
    public int hashCode() {
        return 31 + ((dictionary == null) ? 0 : dictionary.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        KeywordDictionary other = (KeywordDictionary) obj;

        if (dictionary == null) {
            if (other.dictionary != null)
                return false;
        } else if (!dictionary.equals(other.dictionary))
            return false;
            
        return true;
    }

    @Override
    public String toString() {
        return "KeywordDictionary [keywords=" + dictionary.keySet().size() + "]";
    }
}
