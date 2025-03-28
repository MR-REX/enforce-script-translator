package ru.mrrex.estranslator.exception;

public class KeywordAlreadyExistsException extends IllegalStateException {

    public KeywordAlreadyExistsException(String keyword) {
        super(String.format("Keyword \"%s\" is already present in the dictionary", keyword));
    }
}
