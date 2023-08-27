package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class NewlineProcessor {
    private static volatile @Service NewlineProcessor instance;

    public static @Service NewlineProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new NewlineProcessor();
                }
            }
        }
        return instance;
    }

    private NewlineProcessor() {
    }

    /**
     * Splits list of tokens to list of lines using newline token as delimiter.
     */
    public @Mandatory List<List<Token>> process(@Mandatory List<Token> tokens) {
        List<List<Token>> lines = new List<>();
        lines.addLast(new List<>());
        for (Token token : tokens) {
            if (isNewline(token)) {
                lines.addLast(new List<>());
            } else {
                lines.getLast().addLast(token);
            }
        }
        return lines;
    }

    private boolean isNewline(@Mandatory Token token) {
        return token instanceof WhitespaceToken && token.getText().equals("\n");
    }
}
