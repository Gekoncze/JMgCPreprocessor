package cz.mg.c.preprocessor.processors.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.components.MacroConditions;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.directives.*;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.services.MacroExpressionEvaluator;
import cz.mg.c.preprocessor.processors.macro.services.MacroParser;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroProcessor {
    private static volatile @Service MacroProcessor instance;

    public static @Service MacroProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroProcessor();
                    instance.macroParser = MacroParser.getInstance();
                    instance.macroExpressionEvaluator = MacroExpressionEvaluator.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service MacroParser macroParser;
    private @Service MacroExpressionEvaluator macroExpressionEvaluator;

    private MacroProcessor() {
    }

    /**
     * Removes macro definitions from lines and returns list of remaining tokens.
     * Macro definitions are stored into macros parameter.
     * Macro calls are evaluated during the processing.
     */
    public @Mandatory List<Token> process(@Mandatory List<List<Token>> lines, @Mandatory Macros macros) {
        MacroConditions conditions = new MacroConditions();
        MacroExpander expander = new MacroExpander(macros);

        for (List<Token> line : lines) {
            Token directive = findDirective(line);
            if (conditions.isTrue()) {
                processTrueBranch(line, directive, macros, conditions, expander);
            } else if (conditions.isFalse()) {
                processFalseBranch(line, directive, macros, conditions, expander);
            } else if (conditions.isCompleted()) {
                processCompletedBranch(line, directive, macros, conditions, expander);
            } else {
                throw new IllegalStateException();
            }
        }

        conditions.validateIsRoot();
        expander.validateNotExpanding();

        return expander.getTokens();
    }

    private void processTrueBranch(
        @Mandatory List<Token> line,
        @Optional Token directive,
        @Mandatory Macros macros,
        @Mandatory MacroConditions conditions,
        @Mandatory MacroExpander expander
    ) {
        if (directive == null) {
            for (Token token : line) {
                expander.expand(token);
            }
        } else if (directive.getText().equals(IncludeDirective.KEYWORD)) {
            return; // includes are skipped and should be processed separately
        } else if (directive.getText().equals(IfDirective.KEYWORD)) {
            if (macroExpressionEvaluator.evaluateExpression(line, macros)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(IfdefDirective.KEYWORD)) {
            String name = line.get(2).getText();
            if (macros.defined(name)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(IfndefDirective.KEYWORD)) {
            String name = line.get(2).getText();
            if (!macros.defined(name)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(ElifDirective.KEYWORD)) {
            conditions.unnest(directive);
        } else if (directive.getText().equals(ElseDirective.KEYWORD)) {
            conditions.unnest(directive);
        } else if (directive.getText().equals(EndifDirective.KEYWORD)) {
            conditions.unnest(directive);
        } else if (directive.getText().equals(DefineDirective.KEYWORD)) {
            macros.define(macroParser.parse(line));
        } else if (directive.getText().equals(UndefDirective.KEYWORD)) {
            String name = line.get(2).getText();
            macros.undefine(name);
        } else if (directive.getText().equals(ErrorDirective.KEYWORD)) {
            error(line);
        } else if (directive.getText().equals(WarningDirective.KEYWORD)) {
            warning(line);
        } else {
            throw new MacroException(
                directive.getPosition(),
                "Unexpected preprocessor directive '" + directive.getText() +"'."
            );
        }
    }

    private void processFalseBranch(
        @Mandatory List<Token> line,
        @Optional Token directive,
        @Mandatory Macros macros,
        @Mandatory MacroConditions conditions,
        @Mandatory MacroExpander expander
    ) {
        if (directive == null) {
            return;
        } else if (directive.getText().equals(ElifDirective.KEYWORD)) {
            if (macroExpressionEvaluator.evaluateExpression(line, macros)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(ElseDirective.KEYWORD)) {
            conditions.nest(directive);
        } else if (directive.getText().equals(EndifDirective.KEYWORD)) {
            conditions.end();
        }
    }

    private void processCompletedBranch(
        @Mandatory List<Token> line,
        @Optional Token directive,
        @Mandatory Macros macros,
        @Mandatory MacroConditions conditions,
        @Mandatory MacroExpander expander
    ) {
        if (directive == null) {
            return;
        } else if (directive.getText().equals(ElifDirective.KEYWORD)) {
            return;
        } else if (directive.getText().equals(ElseDirective.KEYWORD)) {
            return;
        } else if (directive.getText().equals(EndifDirective.KEYWORD)) {
            conditions.end();
        }
    }

    private @Optional Token findDirective(@Mandatory List<Token> line) {
        if (line.count() >= 2) {
            if (line.getFirst().getText().equals("#")) {
                return line.get(1);
            }
        }
        return null;
    }

    private void error(@Mandatory List<Token> line) {
        throw new MacroException(
            line.get(1).getPosition(),
            "Error directive reached."
        );
    }

    private void warning(@Mandatory List<Token> line) {
        System.out.println("Warning directive reached.");
    }
}
