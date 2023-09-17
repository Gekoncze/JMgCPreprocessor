package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
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
        TokenReader reader = new TokenReader(line, MacroException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        int position = reader.read(NameToken.class).getPosition();

        if (!reader.has()) {
            throw new MacroException(position, "Missing expression.");
        }

        List<Token> expression = new List<>();
        while (reader.has()) {
            expression.addLast(reader.read());
        }
        return expression;
    }
}
