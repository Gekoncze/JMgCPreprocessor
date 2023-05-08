package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.utilities.MacroException;
import cz.mg.c.preprocessor.macro.utilities.MacroExpansion;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NumberToken;

import java.util.Objects;

public @Service class DefinedMacroExpansionService {
    private static @Optional DefinedMacroExpansionService instance;

    public static @Mandatory DefinedMacroExpansionService getInstance() {
        if (instance == null) {
            instance = new DefinedMacroExpansionService();
        }
        return instance;
    }

    private DefinedMacroExpansionService() {
    }

    public @Mandatory List<Token> expand(@Mandatory MacroExpansion expansion, @Mandatory Macros macros) {
        Objects.requireNonNull(expansion.getArguments());
        int tokenCount = expansion.getArguments().getFirst().count();
        if (tokenCount == 1) {
            String name = expansion.getArguments().getFirst().getFirst().getText();
            String result = macros.defined(name) ? "1" : "0";
            return new List<>(new NumberToken(result, expansion.getToken().getPosition()));
        } else {
            throw new MacroException(
                expansion.getToken().getPosition(),
                "Wrong number of tokens for 'defined' macro. Expected 1 token, but got " + tokenCount + "."
            );
        }
    }
}
