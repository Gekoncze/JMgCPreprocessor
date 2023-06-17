package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.Token;

public @Entity class IncludeDirective extends Directive {
    public static final String KEYWORD = "include";

    public IncludeDirective(Token token) {
        super(token);
    }
}
