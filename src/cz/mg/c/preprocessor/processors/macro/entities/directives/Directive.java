package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.tokenizer.entities.Token;

public abstract @Entity class Directive {
    private Token token;

    public Directive(Token token) {
        this.token = token;
    }

    @Required @Shared
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
