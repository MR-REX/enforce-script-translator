package ru.mrrex.estranslator.command.characters;

import picocli.CommandLine.Command;
import ru.mrrex.estranslator.command.template.AbstractDictionariesListCommand;
import ru.mrrex.estranslator.dictionary.DictionaryManager;
import ru.mrrex.estranslator.dictionary.character.CharacterDictionary;
import ru.mrrex.estranslator.dictionary.character.CharacterDictionaryManager;

@Command(name = "list", description = "Displays the list of built-in character dictionaries.")
public class CharacterDictionariesListCommand
        extends AbstractDictionariesListCommand<CharacterDictionary> {

    @Override
    protected DictionaryManager<CharacterDictionary> getDictionaryManager() {
        return CharacterDictionaryManager.INSTANCE;
    }
}
