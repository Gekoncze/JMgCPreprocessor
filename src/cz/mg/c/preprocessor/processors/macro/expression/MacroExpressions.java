package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.system.DefinedMacro;
import cz.mg.collections.list.List;
import cz.mg.token.Token;

public @Service class MacroExpressions {
    private static volatile @Service MacroExpressions instance;

    public static @Service MacroExpressions getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroExpressions();
                    instance.expressions = Expressions.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service Expressions expressions;

    private MacroExpressions() {
    }

    public boolean evaluate(@Mandatory List<Token> expression, @Mandatory MacroManager macros) {
        return macros.temporary(() -> {
            return expressions.evaluate(
                MacroExpander.expand(expression, macros)
            );
        }, new DefinedMacro());
    }
}