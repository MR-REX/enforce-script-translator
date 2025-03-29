package ru.mrrex.estranslator.exception.dictionary.keyword;

public class DefaultKeywordDictionaryAccessException extends RuntimeException {

    public DefaultKeywordDictionaryAccessException(String dictionaryId, Throwable cause) {
        super("Failed to get access to default dictionary with id \"" + dictionaryId + "\"", cause);
    }
}
