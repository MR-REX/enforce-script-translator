package ru.mrrex.estranslator.command;

import picocli.CommandLine.Command;
import ru.mrrex.estranslator.command.dictionary.DictionaryKeywordsCommand;
import ru.mrrex.estranslator.command.dictionary.DictionaryListCommand;

@Command(
    name = "dictionary",
    description = "This section includes commands for working with dictionaries.",
    subcommands = {
        DictionaryKeywordsCommand.class,
        DictionaryListCommand.class
    }
)
public class DictionaryCommand {}
