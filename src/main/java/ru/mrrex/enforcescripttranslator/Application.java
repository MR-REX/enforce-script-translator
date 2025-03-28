package ru.mrrex.enforcescripttranslator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import ru.mrrex.enforcescripttranslator.command.DictionaryCommand;

@Command(
    name = "Enforce Script Translator",
    description = "Allows you to translate scripts with custom keywords into Enforce Script according to the specification.",
    mixinStandardHelpOptions = true,
    subcommands = {
        DictionaryCommand.class
    }
)
public class Application {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }
}
