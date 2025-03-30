package ru.mrrex.estranslator.dictionary.character;

import ru.mrrex.estranslator.dictionary.AbstractDictionary;

public class CharacterDictionary extends AbstractDictionary<Character, String> {

    public void setCharacter(char character, String string) {
        dictionary.put(character, string);
    }

    public void addCharacter(char character, String string) {
        if (dictionary.containsKey(character)) {
            String message = "Character '%s' is already present in the dictionary".formatted(character);
            throw new IllegalArgumentException(message);
        }

        setCharacter(character, string);
    }

    @Override
    public String toString() {
        return "CharacterDictionary [characters=" + size() + "]";
    }
}
