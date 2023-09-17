package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class TokenConcatenationService {
    private static volatile @Service TokenConcatenationService instance;

    public static @Service TokenConcatenationService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new TokenConcatenationService();
                }
            }
        }
        return instance;
    }

    private TokenConcatenationService() {
    }

    public void concatenate(@Mandatory List<Token> tokens) {
        for (ListItem<Token> item = tokens.getFirstItem(); item != null; item = item.getNextItem()) {
            if (isDoubleNumberSign(item.get())) {
                if (item.getPreviousItem() != null && item.getNextItem() != null) {
                    evaluate(item);
                }
            }
        }
    }

    private void evaluate(@Mandatory ListItem<Token> operator) {
        operator.set(concatenate(operator.removePrevious(), operator.removeNext()));
    }

    private @Mandatory Token concatenate(@Mandatory Token left, @Mandatory Token right) {
        if (left instanceof NameToken && right instanceof NameToken) {
            return new NameToken(left.getText() + right.getText(), left.getPosition());
        }

        if (left instanceof NameToken && right instanceof NumberToken) {
            return new NameToken(left.getText() + right.getText(), left.getPosition());
        }

        if (left instanceof NumberToken && right instanceof NumberToken) {
            return new NumberToken(left.getText() + right.getText(), left.getPosition());
        }

        if (left instanceof NumberToken && right instanceof NameToken) {
            return new NumberToken(left.getText() + right.getText(), left.getPosition());
        }

        throw new UnsupportedOperationException(
            "Unsupported combination of token types for concatenation: "
                + left.getClass().getSimpleName()
                + " ## "
                + right.getClass().getSimpleName()
                + "."
        );
    }

    private boolean isDoubleNumberSign(@Mandatory Token token) {
        return token instanceof SpecialToken && token.getText().equals("##");
    }
}