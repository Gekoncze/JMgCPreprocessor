package cz.mg.c.preprocessor.processors.macro.branch;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroBranches;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.directive.DirectiveParsers;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.directives.*;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.expression.MacroExpressions;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroTrueBranchProcessor implements MacroBranchProcessor {
    private static volatile @Service MacroTrueBranchProcessor instance;

    public static @Service MacroTrueBranchProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroTrueBranchProcessor();
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
        if (directive != null) {
            processDirective(directive, macros, branches);
        } else {
            processLine(line, expander);
        }
    }

    private void processLine(@Mandatory List<Token> line, @Mandatory MacroExpander expander) {
        for (Token token : line) {
            expander.addToken(token);
        }
    }

    private void processDirective(
        @Mandatory Directive directive,
        @Mandatory Macros macros,
        @Mandatory MacroBranches branches
    ) {
        if (directive instanceof IncludeDirective) {
            return; // includes are skipped for now
        } else if (directive instanceof IfDirective) {
            List<Token> expression = ((IfDirective) directive).getExpression();
            if (expressions.evaluate(expression, macros)) {
                branches.nest(directive);
            } else {
                branches.skip();
            }
        } else if (directive instanceof IfdefDirective) {
            String name = ((IfdefDirective) directive).getName().getText();
            if (macros.defined(name)) {
                branches.nest(directive);
            } else {
                branches.skip();
            }
        } else if (directive instanceof IfndefDirective) {
            String name = ((IfndefDirective) directive).getName().getText();
            if (!macros.defined(name)) {
                branches.nest(directive);
            } else {
                branches.skip();
            }
        } else if (directive instanceof ElifDirective) {
            branches.unnest(directive);
        } else if (directive instanceof ElseDirective) {
            branches.unnest(directive);
        } else if (directive instanceof EndifDirective) {
            branches.unnest(directive);
            branches.end();
        } else if (directive instanceof DefineDirective) {
            Macro macro = ((DefineDirective) directive).getMacro();
            macros.define(macro);
        } else if (directive instanceof UndefDirective) {
            String name = ((UndefDirective) directive).getName().getText();
            macros.undefine(name);
        } else if (directive instanceof ErrorDirective) {
            error(directive);
        } else if (directive instanceof WarningDirective) {
            warning(directive);
        } else {
            throw new MacroException(
                directive.getKeyword().getPosition(),
                "Unsupported preprocessor directive '" + directive.getKeyword().getText() +"'."
            );
        }
    }

    private void error(@Mandatory Directive directive) {
        throw new MacroException(
            directive.getKeyword().getPosition(),
            "Error directive reached at position " + directive.getKeyword().getPosition() + "."
        );
    }

    private void warning(@Mandatory Directive directive) {
        System.out.println(
            "Warning directive reached at position " + directive.getKeyword().getPosition() + "."
        );
    }
}
