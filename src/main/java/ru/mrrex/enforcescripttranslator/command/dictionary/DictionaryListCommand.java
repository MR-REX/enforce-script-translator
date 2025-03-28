package ru.mrrex.enforcescripttranslator.command.dictionary;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import ru.mrrex.enforcescripttranslator.dictionary.KeywordDictionaryManager;

@Command(
    name = "list",
    description = "Displays the list of built-in dictionaries."
)
public class DictionaryListCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryListCommand.class);

    @Override
    public Integer call() throws Exception {
        KeywordDictionaryManager manager = KeywordDictionaryManager.INSTANCE;

        List<String> embeddedDictionaries = manager.getEmbeddedDictionaries();
        int embeddedDictionariesCount = embeddedDictionaries.size();

        if (embeddedDictionariesCount < 1) {
            logger.warn("There are no built-in dictionaries");
            return ExitCode.OK;
        }

        String listOfEmbeddedDictionaries = embeddedDictionaries.stream()
                .map(d -> '"' + d + '"')
                .collect(Collectors.joining(", "));

        String result = String.format(
            "Available built-in dictionaries (x%d): %s.",
            embeddedDictionariesCount,
            listOfEmbeddedDictionaries
        );

        logger.info(result);

        return ExitCode.OK;
    }
}
