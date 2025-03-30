package ru.mrrex.estranslator.dictionary.keyword;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import ru.mrrex.estranslator.dictionary.AbstractDictionaryReader;
import ru.mrrex.estranslator.exception.DictionaryParseException;

public class KeywordDictionaryReader extends AbstractDictionaryReader<String, List<String>> {

    public KeywordDictionaryReader(Reader reader) {
        super(reader);
    }

    @Override
    protected void processLine(String line, BiConsumer<String, List<String>> consumer)
            throws IOException, DictionaryParseException {
        String[] parts = line.split("\\:\\s*");

        if (parts.length != 2)
            throw new DictionaryParseException("Incorrect line format");

        String[] uncheckedKeywords = parts[1].split("\\,\\s*");

        String value = parts[0];
        List<String> keywords = Stream.of(uncheckedKeywords)
                .map(String::trim)
                .filter(v -> !v.isEmpty())
                .toList();

        consumer.accept(value, keywords);
    }
}
