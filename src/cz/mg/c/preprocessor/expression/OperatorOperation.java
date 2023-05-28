package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Component;

public @Component interface OperatorOperation {
    int evaluate(int a, int b);
}
