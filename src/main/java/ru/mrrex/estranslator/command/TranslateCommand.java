package ru.mrrex.estranslator.command;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Parameters;

@Command(name = "translate",
        description = "",
        mixinStandardHelpOptions = true)
public class TranslateCommand implements Callable<Integer> {

    @Parameters(arity = "1..*", description = "at least one file path to translate",
            paramLabel = "FILES")
    private List<Path> filePaths;

    @Override
    public Integer call() throws Exception {
        return ExitCode.OK;
    }
}
