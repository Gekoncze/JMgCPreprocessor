package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.Token;

public @Entity class ErrorDirective extends Directive {
    public static final String KEYWORD = "error";

    public ErrorDirective() {
    }

    public ErrorDirective(Token keyword) {
        super(keyword);
    }
}
