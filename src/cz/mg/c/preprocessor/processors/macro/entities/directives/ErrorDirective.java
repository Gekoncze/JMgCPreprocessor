package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Entity class ErrorDirective extends Directive {
    public static final String KEYWORD = "error";

    public ErrorDirective() {
    }

    public ErrorDirective(WordToken keyword) {
        super(keyword);
    }
}
