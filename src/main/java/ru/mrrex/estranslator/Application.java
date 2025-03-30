package ru.mrrex.estranslator;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import ru.mrrex.estranslator.command.CharacterDictionaryCommands;
import ru.mrrex.estranslator.command.KeywordDictionaryCommands;
import ru.mrrex.estranslator.command.TranslateCommand;

@Command(
    name = "estranslator",
    description = "Allows you to translate scripts with custom keywords into Enforce Script according to the specification.",
    mixinStandardHelpOptions = true,
    subcommands = {
        KeywordDictionaryCommands.class,
        CharacterDictionaryCommands.class,
        TranslateCommand.class
    }
)
public class Application {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }
}
