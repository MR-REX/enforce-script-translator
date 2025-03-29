package ru.mrrex.estranslator.dictionary.character;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.BiConsumer;
import ru.mrrex.estranslator.exception.dictionary.character.CharacterDictionaryParseException;

public class CharacterDictionaryReader implements AutoCloseable {

    private final BufferedReader bufferedReader;

    public CharacterDictionaryReader(Reader reader) {
        this.bufferedReader = new BufferedReader(reader);
    }

    public void read(BiConsumer<Character, String> consumer)
            throws IOException, CharacterDictionaryParseException {
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("# "))
                continue;

            String[] parts = line.split("\\:\\s*");

            if (parts.length != 2)
                throw new CharacterDictionaryParseException("Incorrect line format");

            char fromCharacter = parts[0].charAt(0);
            String toCharacters = parts[1];

            if (toCharacters.isBlank())
                toCharacters = "";

            consumer.accept(fromCharacter, toCharacters);
        }
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}
