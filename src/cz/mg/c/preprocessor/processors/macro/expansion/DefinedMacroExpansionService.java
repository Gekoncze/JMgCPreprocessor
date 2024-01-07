package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.system.DefinedMacro;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
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
    public @Mandatory List<Token> expand(@Mandatory MacroManager macros, @Mandatory MacroCall call) {
        validate(call);
        Objects.requireNonNull(call.getArguments());
        String name = call.getArguments().getFirst().getFirst().getText();
        String result = macros.defined(name) ? "1" : "0";
        int position = call.getToken().getPosition();
        return new List<>(new NumberToken(result, position));
    }

    private void validate(@Mandatory MacroCall call) {
        Objects.requireNonNull(call.getArguments());
        int tokenCount = call.getArguments().getFirst().count();
        if (tokenCount != 1) {
            throw new MacroException(
                call.getToken().getPosition(),
                "Wrong number of tokens for '" + DefinedMacro.NAME + "' macro. "
                    + "Expected 1 token, but got " + tokenCount + "."
            );
        }
    }
}
