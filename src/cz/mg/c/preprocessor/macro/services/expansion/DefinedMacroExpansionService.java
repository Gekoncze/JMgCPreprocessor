package cz.mg.c.preprocessor.macro.services.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.entities.system.DefinedMacro;
import cz.mg.c.preprocessor.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.macro.components.MacroExpansion;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NumberToken;

import java.util.Objects;

public @Service class DefinedMacroExpansionService implements MacroExpansionService {
    private static volatile @Service DefinedMacroExpansionService instance;

    public static @Service DefinedMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new DefinedMacroExpansionService();
                }
            }
        }
        return instance;
    }

    private DefinedMacroExpansionService() {
    }

    @Override
    public @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroExpansion expansion) {
        validate(expansion);
        Objects.requireNonNull(expansion.getArguments());
        String name = expansion.getArguments().getFirst().getFirst().getText();
        String result = macros.defined(name) ? "1" : "0";
        int position = expansion.getToken().getPosition();
        return new List<>(new NumberToken(result, position));
    }

    private void validate(@Mandatory MacroExpansion expansion) {
        Objects.requireNonNull(expansion.getArguments());
        int tokenCount = expansion.getArguments().getFirst().count();
        if (tokenCount != 1) {
            throw new MacroException(
                expansion.getToken().getPosition(),
                "Wrong number of tokens for '" + DefinedMacro.NAME + "' macro. "
                    + "Expected 1 token, but got " + tokenCount + "."
            );
        }
    }
}
