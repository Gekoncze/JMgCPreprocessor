package cz.mg.c.preprocessor.preprocessors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.CommentToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class TokenFactory {
    private static @Optional TokenFactory instance;

    public static @Mandatory TokenFactory getInstance() {
        if (instance == null) {
            instance = new TokenFactory();
        }
        return instance;
    }

    private TokenFactory() {
    }

    public @Mandatory Token comment(@Mandatory String text) {
        return new CommentToken(text, 0);
    }

    public @Mandatory Token documentation(@Mandatory String text) {
        return new CommentToken(text, 0);
    }

    public @Mandatory Token plain(@Mandatory String text) {
        return new Token(text, 0);
    }

    public @Mandatory Token special(@Mandatory String text) {
        return new SpecialToken(text, 0);
    }

    public @Mandatory Token whitespace(@Mandatory String text) {
        return new WhitespaceToken(text, 0);
    }
}
