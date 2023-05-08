package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class NewlineProcessor {
    private static @Optional NewlineProcessor instance;

    public static @Mandatory NewlineProcessor getInstance() {
        if (instance == null) {
            instance = new NewlineProcessor();
        }
        return instance;
    }

    private NewlineProcessor() {
    }

    /**
     * Splits list of tokens to list of lines using newline delimiter.
     */
    public @Mandatory List<List<Token>> process(@Mandatory List<Token> tokens) {
        List<List<Token>> lines = new List<>();
        lines.addLast(new List<>());
        for (Token token : tokens) {
            if (token.getText().equals("\n")) {
                lines.addLast(new List<>());
            } else {
                lines.getLast().addLast(token);
            }
        }
        return lines;
    }
}
