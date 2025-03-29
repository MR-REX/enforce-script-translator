package ru.mrrex.estranslator.translator;

import ru.mrrex.estranslator.dictionary.keyword.KeywordDictionary;

public class EnfusionScriptTranslator extends AbstractScriptTranslator {

    public EnfusionScriptTranslator(ScriptTranslatorConfiguration config) {
        super(config);
    }

    @Override
    public String translate(String code) {
        KeywordDictionary dictionary = config.getDictionary();

        for (String keyword : dictionary.getKeywords())
            code = code.replace(keyword, dictionary.getValue(keyword));

        return code;
    }
}
