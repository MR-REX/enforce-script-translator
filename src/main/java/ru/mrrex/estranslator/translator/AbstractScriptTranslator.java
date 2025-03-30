package ru.mrrex.estranslator.translator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import ru.mrrex.estranslator.transliterator.Transliterator;

public abstract class AbstractScriptTranslator implements ScriptTranslator {

    protected final ScriptTranslatorConfiguration config;

    protected AbstractScriptTranslator(ScriptTranslatorConfiguration config) {
        this.config = config;
    }

    private String removeSingleLineComments(String code) {
        String commentCharacters = config.getSingleLineCommentCharacters();

        String regularExpression = "^\\s*%s.*$".formatted(Pattern.quote(commentCharacters));
        Pattern pattern = Pattern.compile(regularExpression, Pattern.MULTILINE);

        return pattern.matcher(code).replaceAll("");
    }

    private String removeMultiLineComments(String code) {
        String startCommentCharacters = config.getStartMultiLineCommentCharacters();
        String endCommentCharacters = config.getEndMultiLineCommentCharacters();

        String regularExpression = "^\\s*%s[\\s\\S]*?%s".formatted(
            Pattern.quote(startCommentCharacters),
            Pattern.quote(endCommentCharacters)
        );

        Pattern pattern = Pattern.compile(regularExpression, Pattern.MULTILINE);

        return pattern.matcher(code).replaceAll("");
    }

    private String processComments(String code) {
        if (!config.isShouldRemoveComments())
            return code;

        code = removeSingleLineComments(code);
        code = removeMultiLineComments(code);

        return code;
    }

    private String useTransliterator(String code) {
        Transliterator transliterator = config.getTransliterator();

        if (transliterator == null)
            return code;

        return transliterator.transliterate(code);
    }

    @Override
    public String translate(String code) {
        return code;
    }

    @Override
    public void translate(Path inputFilePath, Path outputFilePath) throws IOException {
        String sourceCode = Files.readString(inputFilePath, StandardCharsets.UTF_8);
        String translatedCode = translate(sourceCode);

        translatedCode = processComments(translatedCode);
        translatedCode = useTransliterator(translatedCode);

        Files.writeString(outputFilePath, translatedCode, StandardCharsets.UTF_8);
    }
}
