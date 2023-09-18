package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class WhitespaceProcessor {
    private static volatile @Service WhitespaceProcessor instance;

    public static @Service WhitespaceProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new WhitespaceProcessor();
                }
            }
        }
        return instance;
    }

    private WhitespaceProcessor() {
    }

    /**
     * Removes whitespace tokens.
     */
    public void process(@Mandatory List<Token> tokens) {
        tokens.removeIf(token -> token instanceof WhitespaceToken);
    }

    /**
     * Removes whitespace tokens from macro definitions.
     */
    public void process(@Mandatory Macros macros) {
        for (Macro macro : macros.getDefinitions()) {
            process(macro.getTokens());
        }

        for (MacroCall call : macros.getCalls()) {
            if (call.getArguments() != null) {
                for (List<Token> argument : call.getArguments()) {
                    process(argument);
                }
            }
        }
    }
}
