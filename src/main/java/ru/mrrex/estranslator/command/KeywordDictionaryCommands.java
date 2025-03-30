package ru.mrrex.estranslator.command;

import picocli.CommandLine.Command;
import ru.mrrex.estranslator.command.keywords.KeywordDictionariesListCommand;
import ru.mrrex.estranslator.command.keywords.KeywordDictionaryDefinedCommand;

@Command(
    name = "keywords",
    description = "This section includes commands for working with keyword dictionaries.",
    subcommands = {
        KeywordDictionariesListCommand.class,
        KeywordDictionaryDefinedCommand.class
    }
)
public class KeywordDictionaryCommands {}
