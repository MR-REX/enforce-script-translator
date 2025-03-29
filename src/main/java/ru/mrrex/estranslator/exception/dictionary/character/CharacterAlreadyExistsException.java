package ru.mrrex.estranslator.exception.dictionary.character;

public class CharacterAlreadyExistsException extends IllegalStateException {

    public CharacterAlreadyExistsException(char character) {
        super(String.format("Character '%s' is already present in the dictionary", character));
    }
}
