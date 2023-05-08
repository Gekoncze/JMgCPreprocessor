package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Utility;
import cz.mg.collections.list.List;

public @Utility class Operators {
    public static final List<List<Operator>> OPERATORS = new List<>(
        new List<>(
            new Operator("+", OperatorType.LUNARY, (a, b) -> +b),
            new Operator("-", OperatorType.LUNARY, (a, b) -> -b),
            new Operator("!", OperatorType.LUNARY, (a, b) -> b == 1 ? 0 : 1),
            new Operator("~", OperatorType.LUNARY, (a, b) -> ~b)
        ),
        new List<>(
            new Operator("*", OperatorType.BINARY, (a, b) -> a * b),
            new Operator("/", OperatorType.BINARY, (a, b) -> a / b),
            new Operator("%", OperatorType.BINARY, (a, b) -> a % b)
        ),
        new List<>(
            new Operator("+", OperatorType.BINARY, (a, b) -> a + b),
            new Operator("-", OperatorType.BINARY, (a, b) -> a - b)
        ),
        new List<>(
            new Operator("<<", OperatorType.BINARY, (a, b) -> a << b),
            new Operator(">>", OperatorType.BINARY, (a, b) -> a >> b)
        ),
        new List<>(
            new Operator("<", OperatorType.BINARY, (a, b) -> a < b ? 1 : 0),
            new Operator("<=", OperatorType.BINARY, (a, b) -> a <= b ? 1 : 0),
            new Operator(">", OperatorType.BINARY, (a, b) -> a > b ? 1 : 0),
            new Operator(">=", OperatorType.BINARY, (a, b) -> a >= b ? 1 : 0)
        ),
        new List<>(
            new Operator("==", OperatorType.BINARY, (a, b) -> a == b ? 1 : 0),
            new Operator("!=", OperatorType.BINARY, (a, b) -> a != b ? 1 : 0)
        ),
        new List<>(new Operator("&", OperatorType.BINARY, (a, b) -> a & b)),
        new List<>(new Operator("^", OperatorType.BINARY, (a, b) -> a ^ b)),
        new List<>(new Operator("|", OperatorType.BINARY, (a, b) -> a | b)),
        new List<>(new Operator("&&", OperatorType.BINARY, (a, b) -> (a != 0 && b != 0) ? 1 : 0)),
        new List<>(new Operator("||", OperatorType.BINARY, (a, b) -> (a != 0 || b != 0) ? 1 : 0))
    );
}
