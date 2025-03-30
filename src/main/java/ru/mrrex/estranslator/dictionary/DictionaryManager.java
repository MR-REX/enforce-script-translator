package ru.mrrex.estranslator.dictionary;

import java.io.IOException;
import java.nio.file.Path;
import ru.mrrex.estranslator.exception.DictionaryParseException;

public interface DictionaryManager<T> {

    T getDictionary(String dictionaryId) throws IOException, DictionaryParseException;
    T getDictionary(Path filePath) throws IOException, DictionaryParseException;
}
