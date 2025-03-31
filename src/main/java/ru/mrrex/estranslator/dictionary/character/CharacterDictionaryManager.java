package ru.mrrex.estranslator.dictionary.character;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import ru.mrrex.estranslator.cache.CacheAccessor;
import ru.mrrex.estranslator.dictionary.DictionaryManager;
import ru.mrrex.estranslator.exception.DictionaryParseException;
import ru.mrrex.estranslator.exception.EmbeddedDictionaryAccessException;
import ru.mrrex.estranslator.util.ResourceLoader;

public enum CharacterDictionaryManager implements DictionaryManager<CharacterDictionary> {

    INSTANCE;

    private final CacheAccessor<CharacterDictionary> cacheAccessor;

    private CharacterDictionaryManager() {
        this.cacheAccessor = new CacheAccessor<>(EMBEDDED_DICTIONARIES_DIRECTORY);
    }

    private static final String EMBEDDED_DICTIONARIES_DIRECTORY = "characters";
    private static final String EMBEDDED_DICTIONARY_EXTENSION = ".char";

    @Override
    public List<String> getEmbeddedDictionaryIds() {
        return List.of("cyrillic2latin");
    }

    private CharacterDictionary readDictionary(CharacterDictionaryReader reader)
            throws IOException, DictionaryParseException {
        CharacterDictionary dictionary = new CharacterDictionary();
        reader.read(dictionary::addCharacter);

        return dictionary;
    }

    private CharacterDictionary loadDictionary(String dictionaryId)
            throws IOException, DictionaryParseException {
        if (!dictionaryId.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Unsafe character dictionary ID cannot be processed");

        String fileName = dictionaryId + EMBEDDED_DICTIONARY_EXTENSION;
        String filePath = Paths.get(EMBEDDED_DICTIONARIES_DIRECTORY, fileName).toString();

        InputStream inputStream = ResourceLoader.getResourceAsStream(filePath);

        if (inputStream == null)
            throw new FileNotFoundException("Embedded character dictionary file not found");

        try (
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CharacterDictionaryReader reader = new CharacterDictionaryReader(streamReader)
        ) {
            return readDictionary(reader);
        }
    }

    private CharacterDictionary loadDictionary(Path filePath)
            throws IOException, DictionaryParseException {
        try (
            FileReader fileReader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8);
            CharacterDictionaryReader reader = new CharacterDictionaryReader(fileReader)
        ) {
            return readDictionary(reader);
        }
    }

    @Override
    public CharacterDictionary getDictionary(String dictionaryId)
            throws IOException, DictionaryParseException {
        String cacheId = "embedded:" + dictionaryId;

        if (cacheAccessor.hasKey(cacheId))
            return cacheAccessor.get(cacheId);

        CharacterDictionary dictionary = loadDictionary(dictionaryId);
        cacheAccessor.set(cacheId, dictionary);

        return dictionary;
    }

    @Override
    public CharacterDictionary getDictionary(Path filePath)
            throws IOException, DictionaryParseException {
        String cacheId = filePath.toString();

        if (cacheAccessor.hasKey(cacheId))
            return cacheAccessor.get(cacheId);

        CharacterDictionary dictionary = loadDictionary(filePath);
        cacheAccessor.set(cacheId, dictionary);

        return dictionary;
    }

    public List<CharacterDictionary> getEmbeddedDictionaries() {
        List<CharacterDictionary> embeddedDictioneries = new ArrayList<>();

        for (String dictionaryId : getEmbeddedDictionaryIds()) {
            try {
                embeddedDictioneries.add(getDictionary(dictionaryId));
            } catch (IOException | DictionaryParseException exception) {
                throw new EmbeddedDictionaryAccessException(dictionaryId, exception);
            }
        }

        return embeddedDictioneries;
    }
}
