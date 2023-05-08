package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class WhitespaceProcessor {
    private static @Optional WhitespaceProcessor instance;

    public static @Mandatory WhitespaceProcessor getInstance() {
        if (instance == null) {
            instance = new WhitespaceProcessor();
        }
        return instance;
    }

    private WhitespaceProcessor() {
    }

    /**
     * Removes whitespace tokens.
     */
    public void process(@Mandatory List<List<Token>> lines) {
        for (List<Token> line : lines) {
            line.removeIf(token -> token instanceof WhitespaceToken);
        }
    }
}