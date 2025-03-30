package ru.mrrex.estranslator.dictionary.keyword;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import ru.mrrex.estranslator.cache.CacheAccessor;
import ru.mrrex.estranslator.dictionary.DictionaryManager;
import ru.mrrex.estranslator.exception.DictionaryParseException;
import ru.mrrex.estranslator.exception.EmbeddedDictionaryAccessException;
import ru.mrrex.estranslator.util.ResourceLoader;

public enum KeywordDictionaryManager implements DictionaryManager<KeywordDictionary> {

    INSTANCE;

    private final CacheAccessor<KeywordDictionary> cacheAccessor;

    private KeywordDictionaryManager() {
        this.cacheAccessor = new CacheAccessor<>(EMBEDDED_DICTIONARIES_DIRECTORY);
    }

    public static final String DEFAULT_DICTIONARY_ID = "default";

    private static final String EMBEDDED_DICTIONARIES_DIRECTORY = "keywords";
    private static final String EMBEDDED_DICTIONARY_EXTENSION = ".dict";

    @Override
    public List<String> getEmbeddedDictionaryIds() {
        return List.of(DEFAULT_DICTIONARY_ID, "vpp");
    }

    private KeywordDictionary readDictionary(KeywordDictionaryReader reader)
            throws IOException, DictionaryParseException {
        KeywordDictionary dictionary = new KeywordDictionary();

        reader.read((value, keywords) -> keywords
                .forEach(keyword -> dictionary.addKeyword(keyword, value)));

        return dictionary;
    }

    private KeywordDictionary loadDictionary(String dictionaryId)
            throws IOException, DictionaryParseException {
        if (!dictionaryId.matches("[a-zA-Z0-9]+"))
            throw new IllegalArgumentException("Unsafe keyword dictionary ID cannot be processed");

        String fileName = dictionaryId + EMBEDDED_DICTIONARY_EXTENSION;
        String filePath = Paths.get(EMBEDDED_DICTIONARIES_DIRECTORY, fileName).toString();

        InputStream inputStream = ResourceLoader.getResourceAsStream(filePath);

        if (inputStream == null)
            throw new FileNotFoundException("Embedded keyword dictionary file not found");

        try (
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            KeywordDictionaryReader reader = new KeywordDictionaryReader(streamReader)
        ) {
            return readDictionary(reader);
        }
    }

    private KeywordDictionary loadDictionary(Path filePath)
            throws IOException, DictionaryParseException {
        try (
            FileReader fileReader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8);
            KeywordDictionaryReader reader = new KeywordDictionaryReader(fileReader)
        ) {
            return readDictionary(reader);
        }
    }

    @Override
    public KeywordDictionary getDictionary(String dictionaryId)
            throws IOException, DictionaryParseException {
        String cacheId = "@Int." + dictionaryId;

        if (cacheAccessor.hasKey(cacheId))
            return cacheAccessor.get(cacheId);

        KeywordDictionary dictionary = loadDictionary(dictionaryId);
        cacheAccessor.set(cacheId, dictionary);

        return dictionary;
    }

    @Override
    public KeywordDictionary getDictionary(Path filePath)
            throws IOException, DictionaryParseException {
        String cacheId = filePath.toString();

        if (cacheAccessor.hasKey(cacheId))
            return cacheAccessor.get(cacheId);

        KeywordDictionary dictionary = loadDictionary(filePath);
        cacheAccessor.set(cacheId, dictionary);

        return dictionary;
    }

    public KeywordDictionary getDefaultDictionary() {
        try {
            return getDictionary(DEFAULT_DICTIONARY_ID);
        } catch (Exception exception) {
            throw new EmbeddedDictionaryAccessException(DEFAULT_DICTIONARY_ID, exception);
        }
    }

    public KeywordDictionary getJointDictionary(String dictionaryId)
            throws IOException, DictionaryParseException {
        String cacheId = "@J.Int." + dictionaryId;

        if (cacheAccessor.hasKey(cacheId))
            return cacheAccessor.get(cacheId);

        KeywordDictionary defaultDictionary = getDefaultDictionary();

        if (dictionaryId.equals(DEFAULT_DICTIONARY_ID))
            return defaultDictionary;

        KeywordDictionary dictionary = getDictionary(dictionaryId);
        KeywordDictionary jointDictionary = dictionary.intersection(defaultDictionary);

        cacheAccessor.set(cacheId, jointDictionary);

        return jointDictionary;
    }

    public KeywordDictionary getJointDictionary(Path filePath)
            throws IOException, DictionaryParseException {
        String cacheId = "@J.Ext." + filePath.toString();

        if (cacheAccessor.hasKey(cacheId))
            return cacheAccessor.get(cacheId);

        KeywordDictionary defaultDictionary = getDefaultDictionary();
        KeywordDictionary dictionary = getDictionary(filePath);

        KeywordDictionary jointDictionary = dictionary.intersection(defaultDictionary);
        cacheAccessor.set(cacheId, jointDictionary);

        return jointDictionary;
    }
}
