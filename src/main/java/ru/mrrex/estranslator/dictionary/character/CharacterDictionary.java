package ru.mrrex.estranslator.dictionary.character;

import java.util.HashMap;
import java.util.Map;
import ru.mrrex.estranslator.exception.dictionary.character.CharacterAlreadyExistsException;

public class CharacterDictionary {

    private final Map<Character, String> dictionary;

    public CharacterDictionary() {
        this.dictionary = new HashMap<>();
    }

    public void setCharacter(char character, String string) {
        dictionary.put(character, string);
    }

    public void addCharacter(char character, String string) {
        if (dictionary.containsKey(character))
            throw new CharacterAlreadyExistsException(character);

        setCharacter(character, string);
    }

    public String getString(char character, String defaultString) {
        return dictionary.getOrDefault(character, defaultString);
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

        CharacterDictionary other = (CharacterDictionary) obj;

        if (dictionary == null) {
            if (other.dictionary != null)
                return false;
        } else if (!dictionary.equals(other.dictionary))
            return false;
            
        return true;
    }

    @Override
    public String toString() {
        return "CharacterDictionary [characters=" + dictionary.size() + "]";
    }
}
