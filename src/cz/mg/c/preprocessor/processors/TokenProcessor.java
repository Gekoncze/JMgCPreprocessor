package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.backslash.BackslashPositionProcessor;
import cz.mg.c.preprocessor.processors.backslash.BackslashProcessor;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.Tokenizer;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class TokenProcessor {
    private static volatile @Service TokenProcessor instance;

    public static @Service TokenProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new TokenProcessor();
                    instance.backslashPositionProcessor = BackslashPositionProcessor.getInstance();
                    instance.backslashProcessor = BackslashProcessor.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service BackslashPositionProcessor backslashPositionProcessor;
    private @Service BackslashProcessor backslashProcessor;

    private TokenProcessor() {
    }

    public @Mandatory List<Token> process(@Mandatory String content, @Mandatory Tokenizer tokenizer) {
        String backslashedContent = backslashProcessor.process(content);
        try {
            List<Token> tokens = tokenizer.tokenize(backslashedContent);
            backslashPositionProcessor.process(content, backslashedContent, tokens);
            return tokens;
        } catch (TokenizeException e) {
            e.setPosition(correctPosition(content, backslashedContent, e.getPosition()));
            throw e;
        }
    }

    private int correctPosition(
        @Mandatory String originalContent,
        @Mandatory String backslashedContent,
        int position
    ) {
        Token token = new Token("", position);
        backslashPositionProcessor.process(originalContent, backslashedContent, new List<>(token));
        return token.getPosition();
    }
}
