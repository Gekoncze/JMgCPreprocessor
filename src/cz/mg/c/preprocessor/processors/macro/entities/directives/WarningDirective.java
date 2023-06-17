package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.tokenizer.entities.Token;

public @Entity class WarningDirective extends Directive {
    public static final String KEYWORD = "warning";

    private String message;

    public WarningDirective(Token token) {
        super(token);
    }

    @Required @Shared
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
