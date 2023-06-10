package cz.mg.c.preprocessor.processors.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.services.AllMacroExpansionService;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Component class MacroExpander {
    private final AllMacroExpansionService allMacroExpansionService = AllMacroExpansionService.getInstance();

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

    public void expand(@Mandatory List<Token> tokens) {
        for (Token token : tokens) {
            expand(token);
        }
    }

    public void expand(@Mandatory Token token) {
        if (call == null || nesting == null) {
            Macro macro = macros.getMap().getOptional(token.getText());
            if (macro != null) {
                if (macro.getParameters() == null) {
                    tokens.addCollectionLast(allMacroExpansionService.expandRecursively(new MacroCall(macro, token, null), macros));
                } else {
                    call = new MacroCall(macro, token, new List<>());
                    nesting = 0;
                }
            } else {
                tokens.addLast(token);
            }
        } else if (call.getArguments() != null) {
            if (call.getArguments().isEmpty()) {
                if (token.getText().equals("(")) {
                    call.getArguments().addLast(new List<>());
                } else {
                    tokens.addLast(call.getToken());
                    call = null;
                    nesting = null;
                }
            } else {
                if (nesting > 0) {
                    if (token.getText().equals("(")) {
                        call.getArguments().getLast().addLast(token);
                        nesting++;
                    } else if (token.getText().equals(")")) {
                        call.getArguments().getLast().addLast(token);
                        nesting--;
                    } else {
                        call.getArguments().getLast().addLast(token);
                    }
                } else {
                    if (token.getText().equals("(")) {
                        call.getArguments().getLast().addLast(token);
                        nesting++;
                    } else if (token.getText().equals(")")) {
                        tokens.addCollectionLast(allMacroExpansionService.expandRecursively(call, macros));
                        call = null;
                        nesting = null;
                    } else if (token.getText().equals(",")) {
                        call.getArguments().addLast(new List<>());
                    } else {
                        call.getArguments().getLast().addLast(token);
                    }
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public void validateNotExpanding() {
        if (call != null) {
            throw new MacroException(
                call.getToken().getPosition(),
                "Unfinished macro expansion."
            );
        }
    }

    public static @Mandatory List<Token> expand(@Mandatory List<Token> tokens, @Mandatory Macros macros) {
        MacroExpander expander = new MacroExpander(macros);
        expander.expand(tokens);
        expander.validateNotExpanding();
        return expander.getTokens();
    }
}
