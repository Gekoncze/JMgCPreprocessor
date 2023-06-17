package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class ExpressionParser {
    private static volatile @Service ExpressionParser instance;

    public static @Service ExpressionParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new ExpressionParser();
                }
            }
        }
        return instance;
    }

    private ExpressionParser() {
    }

    public @Mandatory List<Token> parse(@Mandatory List<Token> line) {
        TokenReader reader = new TokenReader(line, ExpressionException::new);
        reader.read("#");
        int position = reader.read(NameToken.class).getPosition();

        if (!reader.hasNext()) {
            throw new ExpressionException(position, "Missing expression.");
        }

        List<Token> expression = new List<>();
        while (reader.has()) {
            Token token = reader.read();
            if (!(token instanceof WhitespaceToken)) {
                expression.addLast(token);
            }
        }
        return expression;
    }
}
