package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Entity class EndifDirective extends Directive {
    public static final String KEYWORD = "endif";

    public EndifDirective() {
    }

    public EndifDirective(WordToken keyword) {
        super(keyword);
    }
}
