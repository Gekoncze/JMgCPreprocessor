package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.Token;

public @Entity class ElseDirective extends Directive {
    public static final String KEYWORD = "else";

    public ElseDirective(Token token) {
        super(token);
    }
}
