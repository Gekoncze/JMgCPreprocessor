package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Entity class IfDirective extends Directive {
    public static final String KEYWORD = "if";

    private List<Token> expression;

    public IfDirective() {
    }

    public IfDirective(Token token, List<Token> expression) {
        super(token);
        this.expression = expression;
    }

    @Required
    @Shared
    public List<Token> getExpression() {
        return expression;
    }

    public void setExpression(List<Token> expression) {
        this.expression = expression;
    }
}
