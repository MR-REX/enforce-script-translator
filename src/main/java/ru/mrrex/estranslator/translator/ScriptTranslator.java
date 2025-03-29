package ru.mrrex.estranslator.translator;

import java.io.IOException;
import java.nio.file.Path;

public interface ScriptTranslator {

    String translate(String code);
    String processComments(String code);
    void translate(Path inputFilePath, Path outputFilePath) throws IOException;
}
