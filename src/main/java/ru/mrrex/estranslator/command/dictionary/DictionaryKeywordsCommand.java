package ru.mrrex.estranslator.command.dictionary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import ru.mrrex.estranslator.dictionary.KeywordDictionary;
import ru.mrrex.estranslator.dictionary.KeywordDictionaryManager;
import ru.mrrex.estranslator.exception.KeywordDictionaryParseException;

@Command(
    name = "keywords",
    description = "Displays the list of predefined keywords available in the dictionary."
)
public class DictionaryKeywordsCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryKeywordsCommand.class);

    @Option(names = {"-d", "--dict"}, description = "Embedded dictionary ID",
            defaultValue = KeywordDictionaryManager.DEFAULT_DICTIONARY_ID)
    private String dictionaryId;

    @Option(names = {"-f", "--file"}, description = "Path to .dict file")
    private Path filePath;

    @Option(names = {"-s", "--supported"}, description = "Displays only supported keywords")
    private boolean useJointDictionary;

    @Option(names = {"-p", "--pairs"}, description = "Displays keyword-value pairs")
    private boolean asKeywordValuePairs;

    private KeywordDictionary getDictionary() throws IOException, KeywordDictionaryParseException {
        KeywordDictionaryManager manager = KeywordDictionaryManager.INSTANCE;

        if (filePath != null) {
            if (!Files.exists(filePath))
                throw new FileNotFoundException("File not found");

            return useJointDictionary ? manager.getJointDictionary(filePath)
                    : manager.getDictionary(filePath);
        }

        return useJointDictionary ? manager.getJointDictionary(dictionaryId)
                : manager.getDictionary(dictionaryId);
    }

    private void displayKeywords(KeywordDictionary dictionary) {
        if (dictionary.isEmpty()) {
            logger.warn("There are no keywords available.");
            return;
        }

        Set<String> keywords = dictionary.getKeywords();

        String message = (useJointDictionary ? "Supported" : "Defined") + " keywords";
        int keywordsCount = keywords.size();

        String keywordsAsText = keywords.stream()
                .map(k -> '"' + k + '"')
                .collect(Collectors.joining(", "));

        String result = String.format(
            "%s (x%d): %s.",
            message, keywordsCount, keywordsAsText
        );

        logger.info(result);
    }

    private void displayKeywordValuePairs(KeywordDictionary dictionary) {
        if (dictionary.isEmpty()) {
            logger.warn("The dictionary is empty.");
            return;
        }

        logger.info("Number of keywords: {}", dictionary.size());
        logger.info("Keyword-value pairs:");

        dictionary.forEach((keyword, value) -> {
            String pair = String.format("\"%s\" -> \"%s\"", keyword, value);
            logger.info(pair);
        });
    }

    @Override
    public Integer call() throws Exception {
        KeywordDictionary dictionary;

        try {
            dictionary = getDictionary();
        } catch (IOException exception) {
            logger.error("An I/O error occurred while accessing the dictionary file: \"{}\"",
                    exception.getMessage());

            return ExitCode.SOFTWARE;
        } catch (KeywordDictionaryParseException exception) {
            exception.printStackTrace();
            logger.error("An error occurred while parsing the dictionary: \"{}\"",
                    exception.getMessage());

            return ExitCode.SOFTWARE;
        }

        if (asKeywordValuePairs) {
            displayKeywordValuePairs(dictionary);
            return ExitCode.OK;
        }

        displayKeywords(dictionary);

        return ExitCode.OK;
    }
}
