package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.CommentToken;
import cz.mg.tokenizer.entities.tokens.DocumentationToken;

public @Service class CommentProcessor {
    private static @Optional CommentProcessor instance;

    public static @Mandatory CommentProcessor getInstance() {
        if (instance == null) {
            instance = new CommentProcessor();
        }
        return instance;
    }

    private CommentProcessor() {
    }

    /**
     * Removes comment tokens.
     */
    public void process(@Mandatory List<Token> tokens) {
        tokens.removeIf(token -> token instanceof CommentToken || token instanceof DocumentationToken);
    }
}
