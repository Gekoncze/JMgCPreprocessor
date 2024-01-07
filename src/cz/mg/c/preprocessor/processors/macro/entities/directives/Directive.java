package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.tokenizer.entities.tokens.WordToken;

public abstract @Entity class Directive {
    private WordToken keyword;

    public Directive() {
    }

    public Directive(WordToken keyword) {
        this.keyword = keyword;
    }

    @Required @Shared
    public WordToken getKeyword() {
        return keyword;
    }

    public void setKeyword(WordToken keyword) {
        this.keyword = keyword;
    }
}
