package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.token.Token;
import cz.mg.token.tokens.CommentToken;

public @Service class CommentProcessor {
    private static volatile @Service CommentProcessor instance;

    public static @Service CommentProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new CommentProcessor();
                }
            }
        }
        return instance;
    }

    private CommentProcessor() {
    }

    /**
     * Removes comment tokens.
     */
    public void process(@Mandatory List<Token> tokens) {
        tokens.removeIf(token -> token instanceof CommentToken);
    }
}