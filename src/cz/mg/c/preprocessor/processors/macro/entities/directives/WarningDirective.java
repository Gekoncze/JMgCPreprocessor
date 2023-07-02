package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.tokenizer.entities.Token;

public @Entity class WarningDirective extends Directive {
    public static final String KEYWORD = "warning";

    public WarningDirective() {
    }

    public WarningDirective(Token keyword) {
        super(keyword);
    }
}
