package ru.mrrex.estranslator.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.BiConsumer;
import ru.mrrex.estranslator.exception.DictionaryParseException;

public abstract class AbstractDictionaryReader<T, U> implements AutoCloseable {

    protected final BufferedReader bufferedReader;

    protected AbstractDictionaryReader(Reader reader) {
        this.bufferedReader = new BufferedReader(reader);
    }

    public void read(BiConsumer<T, U> consumer) throws IOException, DictionaryParseException {
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("# "))
                continue;

            processLine(line, consumer);
        }
    }

    protected void processLine(String line, BiConsumer<T, U> consumer)
            throws IOException, DictionaryParseException {
        
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}
