package cz.mg.c.preprocessor.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.entities.Macro;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.macro.services.MacroExpansionService;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;
import cz.mg.collections.list.List;

public @Component class MacroExpander {
    private final MacroExpansionService macroExpansionService = MacroExpansionService.getInstance();

    private final @Mandatory Macros macros;
    private final @Mandatory File file;
    private final @Mandatory List<Token> tokens = new List<>();
    private @Optional MacroExpansion expansion;

    public MacroExpander(@Mandatory Macros macros, @Mandatory File file) {
        this.macros = macros;
        this.file = file;
    }

    public @Mandatory List<Token> getTokens() {
        return tokens;
    }

    public void expand(@Mandatory Token token) {
        if (expansion == null) {
            Macro macro = macros.get(token.getText());
            if (macro != null) {
                if (macro.getParameters() == null) {
                    expansion = new MacroExpansion(token, macro, null);
                    tokens.addCollectionLast(macroExpansionService.expandRecursively(expansion, macros, file));
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
                        tokens.addCollectionLast(macroExpansionService.expandRecursively(expansion, macros, file));
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
