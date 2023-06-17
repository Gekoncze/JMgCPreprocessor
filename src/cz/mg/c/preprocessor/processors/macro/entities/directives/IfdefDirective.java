package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.tokenizer.entities.Token;

public @Entity class IfdefDirective extends Directive {
    public static final String KEYWORD = "ifdef";

    private Token name;

    public IfdefDirective() {
    }

    public IfdefDirective(Token token, Token name) {
        super(token);
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
