package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.utilities.TokenReader;
import cz.mg.collections.list.List;

public @Service class ExpressionParser {
    private static @Optional ExpressionParser instance;

    public static @Mandatory ExpressionParser getInstance() {
        if (instance == null) {
            instance = new ExpressionParser();
        }
        return instance;
    }

    private ExpressionParser() {
    }

    public @Mandatory List<Token> parse(@Mandatory List<Token> line) {
        TokenReader reader = new TokenReader(line);
        reader.read("#");
        int position = reader.read().getPosition();

        if (!reader.hasNext()) {
            throw new ExpressionException(position, "Missing expression.");
        }

        List<Token> expression = new List<>();
        while (reader.hasNext()) {
            expression.addLast(reader.read());
        }
        return expression;
    }
}
