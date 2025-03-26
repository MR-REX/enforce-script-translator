package ru.mrrex.enforcescripttranslator.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.mrrex.enforcescripttranslator.exception.KeywordDictionaryParseException;

public class KeywordDictionaryReader implements AutoCloseable {

    private final BufferedReader bufferedReader;

    public KeywordDictionaryReader(Reader reader) {
        this.bufferedReader = new BufferedReader(reader);
    }

    public void read(BiConsumer<String, List<String>> consumer)
            throws IOException, KeywordDictionaryParseException {
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("# "))
                continue;

            String[] parts = line.split(":\\s*");

            if (parts.length != 2)
                throw new KeywordDictionaryParseException("");

            String[] uncheckedValues = parts[1].split(",\\s*");

            String key = parts[0];
            List<String> values = Stream.of(uncheckedValues)
                    .map(String::trim)
                    .filter(v -> !v.isEmpty())
                    .collect(Collectors.toList());

            consumer.accept(key, values);
        }
    }

    @Override
    public void close() throws Exception {
        bufferedReader.close();
    }
}
