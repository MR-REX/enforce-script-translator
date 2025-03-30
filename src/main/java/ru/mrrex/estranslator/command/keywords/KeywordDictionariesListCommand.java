package ru.mrrex.estranslator.command.keywords;

import picocli.CommandLine.Command;
import ru.mrrex.estranslator.command.template.AbstractDictionariesListCommand;
import ru.mrrex.estranslator.dictionary.DictionaryManager;
import ru.mrrex.estranslator.dictionary.keyword.KeywordDictionary;
import ru.mrrex.estranslator.dictionary.keyword.KeywordDictionaryManager;

@Command(name = "list", description = "Displays the list of built-in keyword dictionaries.")
public class KeywordDictionariesListCommand
        extends AbstractDictionariesListCommand<KeywordDictionary> {

    @Override
    protected DictionaryManager<KeywordDictionary> getDictionaryManager() {
        return KeywordDictionaryManager.INSTANCE;
    }
}
