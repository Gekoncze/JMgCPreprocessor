package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
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
     * Splits list of tokens to list of lines using newline token as delimiter.
     * All whitespace tokens are removed.
     */
    public @Mandatory List<List<Token>> process(@Mandatory List<Token> tokens) {
        List<List<Token>> lines = new List<>();
        lines.addLast(new List<>());
        for (Token token : tokens) {
            if (isWhitespace(token)) {
                if (isNewline(token)) {
                    lines.addLast(new List<>());
                }
            } else {
                lines.getLast().addLast(token);
            }
        }
        return lines;
    }

    private boolean isNewline(@Mandatory Token token) {
        return token.getText().equals("\n");
    }

    private boolean isWhitespace(@Mandatory Token token) {
        return token instanceof WhitespaceToken;
    }
}
