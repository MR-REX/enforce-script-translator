package ru.mrrex.estranslator.transliterator;

import java.util.HashMap;
import java.util.Map;
import ru.mrrex.estranslator.dictionary.character.CharacterDictionary;

public class Transliterator {

    private final Map<Character, String> translitMap;

    public Transliterator() {
        this.translitMap = new HashMap<>();
    }

    public void addCharacters(CharacterDictionary dictionary) {
        dictionary.forEach(translitMap::put);
    }

    public String transliterate(String text) {
        if (text == null)
            return null;

        StringBuilder builder = new StringBuilder();
        Character activeQuote = null;

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            if (activeQuote == null && translitMap.containsKey(character)) {
                builder.append(translitMap.get(character));
                continue;
            }

            if (character == '"' || character == '\'')
                activeQuote = (activeQuote == null) ? character : null;

            builder.append(character);
        }

        return builder.toString();
    }
}
