package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.Token;

public @Entity class EndifDirective extends Directive {
    public static final String KEYWORD = "endif";

    public EndifDirective(Token token) {
        super(token);
    }
}
