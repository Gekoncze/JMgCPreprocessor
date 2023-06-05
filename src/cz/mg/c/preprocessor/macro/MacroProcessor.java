package cz.mg.c.preprocessor.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.components.MacroConditions;
import cz.mg.c.preprocessor.macro.components.MacroExpander;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.macro.services.MacroExpressionEvaluator;
import cz.mg.c.preprocessor.macro.services.MacroParser;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroProcessor {
    private static final @Mandatory String INCLUDE = "include";
    private static final @Mandatory String IF = "if";
    private static final @Mandatory String ELIF = "elif";
    private static final @Mandatory String ELSE = "else";
    private static final @Mandatory String IFDEF = "ifdef";
    private static final @Mandatory String IFNDEF = "ifndef";
    private static final @Mandatory String DEFINE = "define";
    private static final @Mandatory String UNDEF = "undef";
    private static final @Mandatory String ENDIF = "endif";
    private static final @Mandatory String ERROR = "error";

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
     * Macros are evaluated during the processing.
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
        } else if (directive.getText().equals(INCLUDE)) {
            return; // includes are skipped and should be processed separately
        } else if (directive.getText().equals(IF)) {
            if (macroExpressionEvaluator.evaluateExpression(line, macros)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(IFDEF)) {
            String name = line.get(2).getText();
            if (macros.defined(name)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(IFNDEF)) {
            String name = line.get(2).getText();
            if (!macros.defined(name)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(ELIF)) {
            conditions.unnest(directive);
        } else if (directive.getText().equals(ELSE)) {
            conditions.unnest(directive);
        } else if (directive.getText().equals(ENDIF)) {
            conditions.unnest(directive);
        } else if (directive.getText().equals(DEFINE)) {
            macros.define(macroParser.parse(line));
        } else if (directive.getText().equals(UNDEF)) {
            String name = line.get(2).getText();
            macros.undefine(name);
        } else if (directive.getText().equals(ERROR)) {
            error(line);
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
        } else if (directive.getText().equals(ELIF)) {
            if (macroExpressionEvaluator.evaluateExpression(line, macros)) {
                conditions.nest(directive);
            } else {
                conditions.skip();
            }
        } else if (directive.getText().equals(ELSE)) {
            conditions.nest(directive);
        } else if (directive.getText().equals(ENDIF)) {
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
        } else if (directive.getText().equals(ELIF)) {
            return;
        } else if (directive.getText().equals(ELSE)) {
            return;
        } else if (directive.getText().equals(ENDIF)) {
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
        if (line.count() == 3) {
            String message = line.get(2).getText();
            throw new MacroException(
                line.get(1).getPosition(),
                "Error directive reached: " + message + "."
            );
        } else {
            throw new MacroException(
                line.get(1).getPosition(),
                "Error directive reached."
            );
        }
    }
}
