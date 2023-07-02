package cz.mg.c.preprocessor.processors.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.expansion.MacroExpansionServices;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.BracketToken;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SeparatorToken;

import java.util.Objects;

public @Component class MacroExpander {
    private final MacroExpansionServices macroExpansionServices = MacroExpansionServices.getInstance();

    private final @Mandatory Macros macros;
    private final @Mandatory List<Token> tokens = new List<>();
    private @Optional MacroCall call;
    private @Optional Integer nesting;

    public MacroExpander(@Mandatory Macros macros) {
        this.macros = macros;
    }

    public @Mandatory List<Token> getTokens() {
        return tokens;
    }

    public void addTokens(@Mandatory List<Token> tokens) {
        for (Token token : tokens) {
            addToken(token);
        }
    }

    public void addToken(@Mandatory Token token) {
        if (call == null || nesting == null) {
            if (!isName(token)) {
                tokens.addLast(token);
            } else {
                Macro macro = macros.getMap().getOptional(token.getText());
                if (macro == null) {
                    tokens.addLast(token);
                } else {
                    call = new MacroCall(macro, token, null);
                    nesting = 0;
                    if (macro.getParameters() == null) {
                        expandCall();
                    }
                }
            }
        } else if (call.getArguments() == null) {
            if (isOpeningBracket(token)) {
                call.setArguments(new List<>());
            } else {
                cancelCall();
            }
        } else if (call.getArguments() != null) {
            if (nesting == 0) {
                if (isOpeningBracket(token)) {
                    nesting++;
                    addArgumentToken(token);
                } else if (isComma(token)) {
                    addArgument();
                } else if (isClosingBracket(token)) {
                    expandCall();
                } else {
                    addArgumentToken(token);
                }
            } else {
                if (isOpeningBracket(token)) {
                    nesting++;
                    addArgumentToken(token);
                } else if (isClosingBracket(token)) {
                    nesting--;
                    addArgumentToken(token);
                } else {
                    addArgumentToken(token);
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    private void expandCall() {
        Objects.requireNonNull(call);
        // TODO - check if not already expanding macro in question
        List<Token> expandedTokens = macroExpansionServices.expand(call, macros);
        call = null;
        nesting = null;
        addTokens(expandedTokens);
    }

    private void cancelCall() {
        Objects.requireNonNull(call);
        tokens.addLast(call.getToken());
        call = null;
        nesting = null;
    }

    private void addArgument() {
        Objects.requireNonNull(call);
        Objects.requireNonNull(call.getArguments());
        call.getArguments().addLast(new List<>());
    }

    private void addArgumentToken(@Mandatory Token token) {
        Objects.requireNonNull(call);
        Objects.requireNonNull(call.getArguments());
        if (call.getArguments().isEmpty()) {
            call.getArguments().addLast(new List<>());
        }
        call.getArguments().getLast().addLast(token);
    }

    private boolean isName(@Mandatory Token token) {
        return token instanceof NameToken;
    }

    private boolean isOpeningBracket(@Mandatory Token token) {
        return token instanceof BracketToken && token.getText().equals("(");
    }

    private boolean isClosingBracket(@Mandatory Token token) {
        return token instanceof BracketToken && token.getText().equals(")");
    }

    private boolean isComma(@Mandatory Token token) {
        return token instanceof SeparatorToken && token.getText().equals(",");
    }

    public void validateNotExpanding() {
        if (call != null) {
            throw new MacroException(
                call.getToken().getPosition(),
                "Missing right parenthesis."
            );
        }
    }

    public static @Mandatory List<Token> expand(@Mandatory List<Token> tokens, @Mandatory Macros macros) {
        MacroExpander expander = new MacroExpander(macros);
        expander.addTokens(tokens);
        expander.validateNotExpanding();
        return expander.getTokens();
    }
}
