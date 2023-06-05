package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.Tokenizer;
import cz.mg.tokenizer.entities.Token;

public @Service class TokenProcessor {
    private static volatile @Service TokenProcessor instance;

    public static @Service TokenProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new TokenProcessor();
                }
            }
        }
        return instance;
    }

    private TokenProcessor() {
    }

    public @Mandatory List<Token> process(@Mandatory String content) {
        return new Tokenizer().tokenize(content);
    }
}
