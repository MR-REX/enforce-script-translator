package ru.mrrex.estranslator.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import ru.mrrex.estranslator.dictionary.keyword.KeywordDictionary;
import ru.mrrex.estranslator.dictionary.keyword.KeywordDictionaryManager;
import ru.mrrex.estranslator.exception.DictionaryParseException;
import ru.mrrex.estranslator.translator.EnfusionScriptTranslator;
import ru.mrrex.estranslator.translator.ScriptTranslator;
import ru.mrrex.estranslator.translator.ScriptTranslatorConfiguration;

@Command(
    name = "translate",
    description = ""
)
public class TranslateCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TranslateCommand.class);

    @Option(names = {"-d", "--dict"}, required = true,
            defaultValue = KeywordDictionaryManager.DEFAULT_DICTIONARY_ID, description = "")
    private String dictionaryId;

    @Option(names = {"-i", "--in", "--input"}, required = true, description = "")
    private Path inputPath;

    @Option(names = {"-o", "--out", "--output"}, required = true, description = "")
    private Path outputPath;

    @Option(names = {"-f", "--forced"}, description = "")
    private boolean isForced;

    @Option(names = {"-r", "--recursive"}, description = "")
    private boolean isRecursive;

    @Option(names = {"-l", "--depth"}, defaultValue = "" + Integer.MAX_VALUE, description = "")
    private int maxRecursionDepth;

    @Option(names = {"-c", "--clear"}, description = "")
    private boolean shouldRemoveComments;

    private KeywordDictionary getDictionary() {
        KeywordDictionaryManager manager = KeywordDictionaryManager.INSTANCE;
        Path filePath = Path.of(dictionaryId);

        try {
            if (Files.isRegularFile(filePath))
                return manager.getDictionary(filePath);

            return manager.getDictionary(dictionaryId);
        } catch (IOException | DictionaryParseException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private ScriptTranslatorConfiguration createTranslatorConfig() {
        ScriptTranslatorConfiguration config = new ScriptTranslatorConfiguration();
        config.setDictionary(getDictionary());
        config.setSingleLineCommentCharacters("//");
        config.setStartMultiLineCommentCharacters("/*");
        config.setEndMultiLineCommentCharacters("*/");
        config.setShouldRemoveComments(shouldRemoveComments);

        return config;
    }

    private ScriptTranslator createTranslator() {
        return new EnfusionScriptTranslator(createTranslatorConfig());
    }

    private int processFile(Path inputFilePath, Path outputFilePath) {
        try {
            ScriptTranslator translator = createTranslator();
            translator.translate(inputFilePath, outputFilePath);
        } catch (IOException exception) {
            logger.error("An error has occurred: \"{}\".", exception.getMessage());
            return ExitCode.SOFTWARE;
        }

        return ExitCode.OK;
    }

    private int processDirectory(Path inputDirectoryPath, Path outputDirectoryPath) {
        int maxDepth = isRecursive ? maxRecursionDepth : 0;

        try (Stream<Path> stream = Files.walk(inputDirectoryPath, maxDepth)) {
            stream.filter(Files::isRegularFile).forEach(filePath -> {
                try {
                    logger.info("Processing \"{}\"...", filePath);

                    Path relativePath = inputDirectoryPath.relativize(filePath);
                    Path destinationPath = outputDirectoryPath.resolve(relativePath);

                    Files.createDirectories(destinationPath.getParent());
                    processFile(filePath, destinationPath);
                } catch (IOException exception) {
                    logger.error("An error occurred when processing the path \"{}\": \"{}\".", filePath,
                            exception.getMessage());
                }
            });
        } catch (IOException exception) {
            logger.error("An error has occurred: \"{}\".", exception.getMessage());
        }

        return ExitCode.OK;
    }

    @Override
    public Integer call() throws Exception {
        boolean isRegularFile = Files.isRegularFile(inputPath);
        boolean isDirectory = Files.isDirectory(inputPath);

        if (!isRegularFile && !isDirectory) {
            logger.warn("Object not found: {}", inputPath);
            return ExitCode.SOFTWARE;
        }

        if (Files.exists(outputPath) && !isForced) {
            String outputObjectType = isRegularFile ? "file" : "directory";
            logger.warn("Output {} already exists: {}", outputObjectType, outputPath);

            return ExitCode.SOFTWARE;
        }

        if (isDirectory) {
            int exitCode = processDirectory(inputPath, outputPath);
            logger.info("Directory processing completed.");

            return exitCode;
        }

        int exitCode = processFile(inputPath, outputPath);
        logger.info("File processing completed.");

        return exitCode;
    }
}
