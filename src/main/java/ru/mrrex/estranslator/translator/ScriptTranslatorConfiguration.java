package ru.mrrex.estranslator.translator;

import ru.mrrex.estranslator.dictionary.keyword.KeywordDictionary;

public class ScriptTranslatorConfiguration {

    private KeywordDictionary dictionary;

    private String singleLineCommentCharacters;
    private String startMultiLineCommentCharacters;
    private String endMultiLineCommentCharacters;

    private boolean shouldRemoveComments;

    public KeywordDictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(KeywordDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String getSingleLineCommentCharacters() {
        return singleLineCommentCharacters;
    }

    public void setSingleLineCommentCharacters(String singleLineCommentSequence) {
        this.singleLineCommentCharacters = singleLineCommentSequence;
    }

    public String getStartMultiLineCommentCharacters() {
        return startMultiLineCommentCharacters;
    }

    public void setStartMultiLineCommentCharacters(String startMultiLineCommentCharacters) {
        this.startMultiLineCommentCharacters = startMultiLineCommentCharacters;
    }

    public String getEndMultiLineCommentCharacters() {
        return endMultiLineCommentCharacters;
    }

    public void setEndMultiLineCommentCharacters(String endMultiLineCommentCharacters) {
        this.endMultiLineCommentCharacters = endMultiLineCommentCharacters;
    }

    public boolean isShouldRemoveComments() {
        return shouldRemoveComments;
    }

    public void setShouldRemoveComments(boolean shouldRemoveComments) {
        this.shouldRemoveComments = shouldRemoveComments;
    }
}
