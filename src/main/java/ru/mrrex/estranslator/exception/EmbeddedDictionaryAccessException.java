package ru.mrrex.estranslator.exception;

public class EmbeddedDictionaryAccessException extends RuntimeException {

    public EmbeddedDictionaryAccessException(String dictionaryId, Throwable cause) {
        super("Failed to get access to embedded dictionary with id \"" + dictionaryId + "\"", cause);
    }
}
