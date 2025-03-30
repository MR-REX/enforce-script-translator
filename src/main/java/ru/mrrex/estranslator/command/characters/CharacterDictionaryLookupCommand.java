package ru.mrrex.estranslator.command.characters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import ru.mrrex.estranslator.dictionary.character.CharacterDictionary;
import ru.mrrex.estranslator.dictionary.character.CharacterDictionaryManager;
import ru.mrrex.estranslator.exception.DictionaryParseException;
import ru.mrrex.estranslator.exception.EmbeddedDictionaryAccessException;

@Command(
    name = "lookup",
    description = "Displays a list of all characters processed by the transliterator."
)
public class CharacterDictionaryLookupCommand implements Callable<Integer> {

    private static final Logger logger =
            LoggerFactory.getLogger(CharacterDictionaryLookupCommand.class);

    @Option(names = {"-f", "--files"}, description = "Path to .char files")
    private String filePaths;

    private List<CharacterDictionary> getCharacterDictionaries() {
        CharacterDictionaryManager manager = CharacterDictionaryManager.INSTANCE;
        List<CharacterDictionary> dictionaries = new ArrayList<>();

        for (String dictionaryId : manager.getEmbeddedDictionaryIds()) {
            try {
                dictionaries.add(manager.getDictionary(dictionaryId));
            } catch (IOException | DictionaryParseException exception) {
                throw new EmbeddedDictionaryAccessException(dictionaryId, exception);
            }
        }

        if (filePaths != null) {
            var stream = Stream.of(filePaths.split(File.pathSeparator));

            stream.map(Path::of).filter(Files::isRegularFile).forEach(path -> {
                try {
                    CharacterDictionary dictionary = manager.getDictionary(path);
                    dictionaries.add(dictionary);
                } catch (IOException | DictionaryParseException exception) {
                    logger.warn("Failed to load characters file \"{}\": \"{}\".", path,
                            exception.getMessage());
                }
            });
        }

        return dictionaries;
    }

    private Map<Character, String> getCharactersMap() {
        Map<Character, String> map = new HashMap<>();

        for (CharacterDictionary dictionary : getCharacterDictionaries())
            dictionary.forEach(map::put);

        return map;
    }

    @Override
    public Integer call() throws Exception {
        Map<Character, String> map = getCharactersMap();

        if (map.isEmpty()) {
            logger.warn("There are no characters available.");
            return ExitCode.OK;
        }

        logger.info("List of tracked characters (x{}):", map.size());

        for (Map.Entry<Character, String> entry : map.entrySet()) {
            char character = entry.getKey();
            String string = entry.getValue();

            if (string != null && !string.isEmpty())
                string = "\"%s\"".formatted(string);
            else
                string = "Empty";

            logger.info("'{}' -> {}", character, string);
        }

        return ExitCode.OK;
    }
}
