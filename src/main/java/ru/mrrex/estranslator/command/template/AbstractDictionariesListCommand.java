package ru.mrrex.estranslator.command.template;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.ExitCode;
import ru.mrrex.estranslator.dictionary.DictionaryManager;

public abstract class AbstractDictionariesListCommand<T> implements Callable<Integer> {

    private static final Logger logger =
            LoggerFactory.getLogger(AbstractDictionariesListCommand.class);

    protected DictionaryManager<T> getDictionaryManager() {
        return null;
    }

    @Override
    public Integer call() throws Exception {
        DictionaryManager<T> manager = getDictionaryManager();

        List<String> embeddedDictionaries = manager.getEmbeddedDictionaryIds();
        int embeddedDictionariesCount = embeddedDictionaries.size();

        if (embeddedDictionariesCount < 1) {
            logger.warn("There are no built-in dictionaries.");
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
