package ru.mrrex.estranslator.dictionary.character;

import java.io.IOException;
import java.io.Reader;
import java.util.function.BiConsumer;
import ru.mrrex.estranslator.dictionary.AbstractDictionaryReader;
import ru.mrrex.estranslator.exception.DictionaryParseException;

public class CharacterDictionaryReader extends AbstractDictionaryReader<Character, String> {

    public CharacterDictionaryReader(Reader reader) {
        super(reader);
    }

    @Override
    protected void processLine(String line, BiConsumer<Character, String> consumer)
            throws IOException, DictionaryParseException {
        String[] parts = line.split("\\:\\s*");

        if (parts.length != 2)
            throw new DictionaryParseException("Incorrect line format");

        char fromCharacter = parts[0].charAt(0);
        String toCharacters = parts[1];

        if (toCharacters.isBlank())
            toCharacters = "";

        consumer.accept(fromCharacter, toCharacters);
    }
}
