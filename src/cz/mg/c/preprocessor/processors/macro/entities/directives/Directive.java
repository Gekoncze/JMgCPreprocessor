package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.tokenizer.entities.Token;

public abstract @Entity class Directive {
    private Token keyword;

    public Directive() {
    }

    public Directive(Token keyword) {
        this.keyword = keyword;
    }

    @Required @Shared
    public Token getKeyword() {
        return keyword;
    }

    public void setKeyword(Token keyword) {
        this.keyword = keyword;
    }
}
