package ru.mrrex.estranslator.command;

import picocli.CommandLine.Command;
import ru.mrrex.estranslator.command.characters.CharacterDictionariesListCommand;
import ru.mrrex.estranslator.command.characters.CharacterDictionaryLookupCommand;

@Command(
    name = "characters",
    description = "This section includes commands for working with character dictionaries.",
    subcommands = {
        CharacterDictionariesListCommand.class,
        CharacterDictionaryLookupCommand.class
    }
)
public class CharacterDictionaryCommands {}
