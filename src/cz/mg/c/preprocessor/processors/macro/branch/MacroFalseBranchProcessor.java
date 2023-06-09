package cz.mg.c.preprocessor.processors.macro.branch;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroBranches;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.directives.Directive;
import cz.mg.c.preprocessor.processors.macro.entities.directives.ElifDirective;
import cz.mg.c.preprocessor.processors.macro.entities.directives.ElseDirective;
import cz.mg.c.preprocessor.processors.macro.entities.directives.EndifDirective;
import cz.mg.c.preprocessor.processors.macro.directive.DirectiveParsers;
import cz.mg.c.preprocessor.processors.macro.expression.MacroExpressions;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroFalseBranchProcessor implements MacroBranchProcessor {
    private static volatile @Service MacroFalseBranchProcessor instance;

    public static @Service MacroFalseBranchProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroFalseBranchProcessor();
                    instance.parsers = DirectiveParsers.getInstance();
                    instance.expressions = MacroExpressions.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service DirectiveParsers parsers;
    private @Service MacroExpressions expressions;

    @Override
    public void process(
        @Mandatory List<Token> line,
        @Mandatory Macros macros,
        @Mandatory MacroBranches branches,
        @Mandatory MacroExpander expander
    ) {
        Directive directive = parsers.parse(line);
        if (directive instanceof ElifDirective) {
            List<Token> expression = ((ElifDirective) directive).getExpression();
            if (expressions.evaluate(expression, macros)) {
                branches.nest(directive);
            } else {
                branches.skip();
            }
        } else if (directive instanceof ElseDirective) {
            branches.nest(directive);
        } else if (directive instanceof EndifDirective) {
            branches.end();
        }
    }
}
