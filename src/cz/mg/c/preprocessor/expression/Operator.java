package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

public @Utility class Operator {
    private final @Mandatory String text;
    private final @Mandatory OperatorType type;
    private final @Mandatory OperatorOperation operation;

    public Operator(
        @Mandatory String text,
        @Mandatory OperatorType type,
        @Mandatory OperatorOperation operation
    ) {
        this.text = text;
        this.type = type;
        this.operation = operation;
    }

    public @Mandatory String getText() {
        return text;
    }

    public @Mandatory OperatorType getType() {
        return type;
    }

    public @Mandatory OperatorOperation getOperation() {
        return operation;
    }
}
