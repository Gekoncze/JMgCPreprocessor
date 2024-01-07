package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Entity class UndefDirective extends Directive {
    public static final String KEYWORD = "undef";

    private Token name;

    public UndefDirective() {
    }

    public UndefDirective(WordToken keyword, Token name) {
        super(keyword);
        this.name = name;
    }

    @Required @Shared
    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }
}
