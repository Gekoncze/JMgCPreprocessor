package cz.mg.c.preprocessor.processors.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.services.AllMacroExpansionService;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Component class MacroExpander {
    private final AllMacroExpansionService allMacroExpansionService = AllMacroExpansionService.getInstance();

    private final @Mandatory Macros macros;
    private final @Mandatory List<Token> tokens = new List<>();
    private @Optional MacroExpansion expansion;

    public MacroExpander(@Mandatory Macros macros) {
        this.macros = macros;
    }

    public @Mandatory List<Token> getTokens() {
        return tokens;
    }

    public void expand(@Mandatory Token token) {
        if (expansion == null) {
            Macro macro = macros.getMap().getOptional(token.getText());
            if (macro != null) {
                if (macro.getParameters() == null) {
                    expansion = new MacroExpansion(token, macro, null);
                    tokens.addCollectionLast(allMacroExpansionService.expandRecursively(expansion, macros));
                    expansion = null;
                } else {
                    expansion = new MacroExpansion(token, macro, new List<>());
                }
            } else {
                tokens.addLast(token);
            }
        } else if (expansion.getArguments() != null) {
            if (expansion.getArguments().isEmpty()) {
                if (token.getText().equals("(")) {
                    expansion.getArguments().addLast(new List<>());
                } else {
                    tokens.addLast(expansion.getToken());
                    expansion = null;
                }
            } else {
                if (expansion.getNesting() > 0) {
                    if (token.getText().equals("(")) {
                        expansion.getArguments().getLast().addLast(token);
                        expansion.setNesting(expansion.getNesting() + 1);
                    } else if (token.getText().equals(")")) {
                        expansion.getArguments().getLast().addLast(token);
                        expansion.setNesting(expansion.getNesting() - 1);
                    } else {
                        expansion.getArguments().getLast().addLast(token);
                    }
                } else {
                    if (token.getText().equals("(")) {
                        expansion.getArguments().getLast().addLast(token);
                        expansion.setNesting(expansion.getNesting() + 1);
                    } else if (token.getText().equals(")")) {
                        tokens.addCollectionLast(allMacroExpansionService.expandRecursively(expansion, macros));
                        expansion = null;
                    } else if (token.getText().equals(",")) {
                        expansion.getArguments().addLast(new List<>());
                    } else {
                        expansion.getArguments().getLast().addLast(token);
                    }
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public void validateNotExpanding() {
        if (expansion != null) {
            throw new MacroException(
                expansion.getToken().getPosition(),
                "Unfinished macro expansion."
            );
        }
    }
}