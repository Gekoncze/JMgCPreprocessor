package cz.mg.c.preprocessor.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.expression.ExpressionProcessor;
import cz.mg.c.preprocessor.expression.ExpressionException;
import cz.mg.c.preprocessor.expression.ExpressionParser;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.services.MacroParser;
import cz.mg.c.preprocessor.macro.utilities.MacroConditions;
import cz.mg.c.preprocessor.macro.utilities.MacroException;
import cz.mg.c.preprocessor.macro.utilities.MacroExpander;
import cz.mg.c.preprocessor.macro.utilities.SystemMacros;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroProcessor {
    public static final String INCLUDE = "include";
    public static final String IF = "if";
    public static final String ELIF = "elif";
    public static final String ELSE = "else";
    public static final String IFDEF = "ifdef";
    public static final String IFNDEF = "ifndef";
    public static final String DEFINE = "define";
    public static final String UNDEF = "undef";
    public static final String ENDIF = "endif";
    public static final String ERROR = "error";

    private static @Optional MacroProcessor instance;

    public static @Mandatory MacroProcessor getInstance() {
        if (instance == null) {
            instance = new MacroProcessor();
            instance.macroParser = MacroParser.getInstance();
            instance.expressionParser = ExpressionParser.getInstance();
            instance.expressionProcessor = ExpressionProcessor.getInstance();
        }
        return instance;
    }

    private MacroParser macroParser;
    private ExpressionParser expressionParser;
    private ExpressionProcessor expressionProcessor;

    private MacroProcessor() {
    }

    /**
     * Removes macro definitions from lines and returns list of remaining tokens.
     * Macro definitions are stored into macros parameter.
     * Macros are evaluated during the processing.
     */
    public @Mandatory List<Token> process(
        @Mandatory List<List<Token>> lines,
        @Mandatory Macros macros,
        @Mandatory File file
    ) {
        MacroConditions conditions = new MacroConditions();
        MacroExpander expander = new MacroExpander(macros, file);

        for (List<Token> line : lines) {
            Token directive = findDirective(line);
            if (conditions.isTrue()) {
                processTrueBranch(line, directive, macros, file, conditions, expander);
            } else if (conditions.isFalse()) {
                processFalseBranch(line, directive, macros, file, conditions, expander);
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
        @Mandatory File file,
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
            if (evaluateExpression(line, macros, file)) {
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
        @Mandatory File file,
        @Mandatory MacroConditions conditions,
        @Mandatory MacroExpander expander
    ) {
        if (directive == null) {
            return;
        } else if (directive.getText().equals(ELIF)) {
            if (evaluateExpression(line, macros, file)) {
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

    private boolean evaluateExpression(
        @Mandatory List<Token> line,
        @Mandatory Macros macros,
        @Mandatory File file
    ) {
        try {
            return expressionProcessor.evaluate(
                expandExpressionMacros(
                    expressionParser.parse(line),
                    macros,
                    file
                )
            );
        } catch (ExpressionException e) {
            throw new MacroException(
                e.getPosition() != -1
                    ? e.getPosition()
                    : line.get(1).getPosition(),
                "Could not evaluate condition.",
                e
            );
        }
    }

    private @Mandatory List<Token> expandExpressionMacros(
        @Mandatory List<Token> tokens,
        @Mandatory Macros macros,
        @Mandatory File file
    ) {
        macros.define(SystemMacros.DEFINED);

        MacroExpander expander = new MacroExpander(macros, file);
        for (Token token : tokens) {
            expander.expand(token);
        }
        expander.validateNotExpanding();

        macros.undefine(SystemMacros.DEFINED.getName().getText());

        return expander.getTokens();
    }
}
