package ru.mrrex.estranslator.dictionary.keyword;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import ru.mrrex.estranslator.exception.dictionary.keyword.DefaultKeywordDictionaryAccessException;
import ru.mrrex.estranslator.exception.dictionary.keyword.KeywordDictionaryParseException;

public enum KeywordDictionaryManager {

    INSTANCE;

    private final HashMap<String, KeywordDictionary> dictionaries;

    private KeywordDictionaryManager() {
        this.dictionaries = new HashMap<>();
    }

    public static final String DEFAULT_DICTIONARY_ID = "default";

    private static final String DEFAULT_DICTIONARIES_DIRECTORY = "dictionaries";
    private static final String DEFAULT_DICTIONARY_EXTENSION = ".dict";

    private static final List<String> DEFAULT_DICTIONARIES = List.of(
        DEFAULT_DICTIONARY_ID, "vpp"
    );

    private KeywordDictionary readDictionary(KeywordDictionaryReader reader)
            throws IOException, KeywordDictionaryParseException {
        KeywordDictionary dictionary = new KeywordDictionary();

        reader.read((value, keywords) -> keywords
                .forEach(keyword -> dictionary.addKeyword(keyword, value)));

        return dictionary;
    }

    private KeywordDictionary loadDictionary(String dictionaryId)
            throws IOException, KeywordDictionaryParseException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null)
            classLoader = getClass().getClassLoader();

        String fileName = dictionaryId + DEFAULT_DICTIONARY_EXTENSION;
        String filePath = Paths.get(DEFAULT_DICTIONARIES_DIRECTORY, fileName).toString();

        InputStream inputStream = classLoader.getResourceAsStream(filePath);

        if (inputStream == null)
            throw new FileNotFoundException("Dictionary file not found");

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                KeywordDictionaryReader reader = new KeywordDictionaryReader(streamReader)) {
            return readDictionary(reader);
        }
    }

    private KeywordDictionary loadDictionary(Path filePath)
            throws IOException, KeywordDictionaryParseException {
        try (FileReader fileReader = new FileReader(filePath.toFile(), StandardCharsets.UTF_8);
                KeywordDictionaryReader reader = new KeywordDictionaryReader(fileReader)) {
            return readDictionary(reader);
        }
    }

    public KeywordDictionary getDictionary(String dictionaryId)
            throws IOException, KeywordDictionaryParseException {
        String cacheId = "@Int." + dictionaryId;

        if (dictionaries.containsKey(cacheId))
            return dictionaries.get(cacheId);

        KeywordDictionary dictionary = loadDictionary(dictionaryId);
        dictionaries.put(cacheId, dictionary);

        return dictionary;
    }

    public KeywordDictionary getDictionary(Path filePath)
            throws IOException, KeywordDictionaryParseException {
        String cacheId = filePath.toString();

        if (dictionaries.containsKey(cacheId))
            return dictionaries.get(cacheId);

        KeywordDictionary dictionary = loadDictionary(filePath);
        dictionaries.put(cacheId, dictionary);

        return dictionary;
    }

    public KeywordDictionary getDefaultDictionary() {
        try {
            return getDictionary(DEFAULT_DICTIONARY_ID);
        } catch (Exception exception) {
            throw new DefaultKeywordDictionaryAccessException(DEFAULT_DICTIONARY_ID, exception);
        }
    }

    public KeywordDictionary getJointDictionary(String dictionaryId)
            throws IOException, KeywordDictionaryParseException {
        String cacheId = "@J.Int." + dictionaryId;

        if (dictionaries.containsKey(cacheId))
            return dictionaries.get(cacheId);

        KeywordDictionary defaultDictionary = getDefaultDictionary();

        if (dictionaryId.equals(DEFAULT_DICTIONARY_ID))
            return defaultDictionary;

        KeywordDictionary dictionary = getDictionary(dictionaryId);
        KeywordDictionary jointDictionary = dictionary.intersection(defaultDictionary);

        dictionaries.put(cacheId, jointDictionary);

        return jointDictionary;
    }

    public KeywordDictionary getJointDictionary(Path filePath)
            throws IOException, KeywordDictionaryParseException {
        String cacheId = "@J.Ext." + filePath.toString();

        if (dictionaries.containsKey(cacheId))
            return dictionaries.get(cacheId);

        KeywordDictionary defaultDictionary = getDefaultDictionary();
        KeywordDictionary dictionary = getDictionary(filePath);

        KeywordDictionary jointDictionary = dictionary.intersection(defaultDictionary);
        dictionaries.put(cacheId, jointDictionary);

        return jointDictionary;
    }

    public List<String> getEmbeddedDictionaries() {
        return DEFAULT_DICTIONARIES;
    }
}
