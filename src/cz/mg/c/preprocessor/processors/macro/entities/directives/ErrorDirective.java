package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;
import cz.mg.tokenizer.entities.Token;

public @Entity class ErrorDirective extends Directive {
    public static final String KEYWORD = "error";

    private String message;

    public ErrorDirective(Token token) {
        super(token);
    }

    @Required @Value
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
