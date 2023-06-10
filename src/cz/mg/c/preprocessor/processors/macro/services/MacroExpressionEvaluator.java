package cz.mg.c.preprocessor.processors.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionEvaluator;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionException;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionParser;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.DefinedMacro;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroExpressionEvaluator {
    private static volatile @Service MacroExpressionEvaluator instance;

    public static @Service MacroExpressionEvaluator getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroExpressionEvaluator();
                    instance.expressionParser = ExpressionParser.getInstance();
                    instance.expressionEvaluator = ExpressionEvaluator.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service ExpressionParser expressionParser;
    private @Service ExpressionEvaluator expressionEvaluator;

    private MacroExpressionEvaluator() {
    }

    public boolean evaluateExpression(@Mandatory List<Token> line, @Mandatory Macros macros) {
        return Macros.temporary(macros, new DefinedMacro(), () -> {
            return expressionEvaluator.evaluate(
                MacroExpander.expand(
                    expressionParser.parse(line),
                    macros
                )
            );
        });
    }
}
