package ru.mrrex.enforcescripttranslator.command;

import picocli.CommandLine.Command;
import ru.mrrex.enforcescripttranslator.command.dictionary.DictionaryKeywordsCommand;
import ru.mrrex.enforcescripttranslator.command.dictionary.DictionaryListCommand;

@Command(
    name = "dictionary",
    description = "This section includes commands for working with dictionaries.",
    subcommands = {
        DictionaryKeywordsCommand.class,
        DictionaryListCommand.class
    }
)
public class DictionaryCommand {}
