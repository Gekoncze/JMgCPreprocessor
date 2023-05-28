package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.*;

public @Service class TokenFactory {
    private static volatile @Service TokenFactory instance;

    public static @Service TokenFactory getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new TokenFactory();
                }
            }
        }
        return instance;
    }

    private TokenFactory() {
    }

    public @Mandatory BracketToken bracket(@Mandatory String text) {
        return new BracketToken(text, 0);
    }

    public @Mandatory CommentToken comment(@Mandatory String text) {
        return new CommentToken(text, 0);
    }

    public @Mandatory NameToken name(@Mandatory String text) {
        return new NameToken(text, 0);
    }

    public @Mandatory NumberToken number(@Mandatory String text) {
        return new NumberToken(text, 0);
    }

    public @Mandatory OperatorToken operator(@Mandatory String text) {
        return new OperatorToken(text, 0);
    }

    public @Mandatory Token plain(@Mandatory String text) {
        return new Token(text, 0);
    }

    public @Mandatory SpecialToken special(@Mandatory String text) {
        return new SpecialToken(text, 0);
    }

    public @Mandatory WhitespaceToken whitespace(@Mandatory String text) {
        return new WhitespaceToken(text, 0);
    }
}
