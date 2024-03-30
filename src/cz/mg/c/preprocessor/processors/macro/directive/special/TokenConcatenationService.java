package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.token.Token;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.NumberToken;
import cz.mg.token.tokens.WhitespaceToken;

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
                    evaluateWithSpaces(item);
                }
            }
        }
    }

    private void evaluateWithSpaces(@Mandatory ListItem<Token> operator) {
        while (isWhitespace(operator.getPreviousItem())) {
            operator.removePrevious();
        }

        while (isWhitespace(operator.getNextItem())) {
            operator.removeNext();
        }

        evaluateWithoutSpaces(operator);
    }

    private void evaluateWithoutSpaces(@Mandatory ListItem<Token> operator) {
        operator.set(concatenate(operator.removePrevious(), operator.removeNext()));
    }

    private @Mandatory Token concatenate(@Mandatory Token left, @Mandatory Token right) {
        if (left instanceof WordToken && right instanceof WordToken) {
            return new WordToken(left.getText() + right.getText(), left.getPosition());
        }

        if (left instanceof WordToken && right instanceof NumberToken) {
            return new WordToken(left.getText() + right.getText(), left.getPosition());
        }

        if (left instanceof NumberToken && right instanceof NumberToken) {
            return new NumberToken(left.getText() + right.getText(), left.getPosition());
        }

        if (left instanceof NumberToken && right instanceof WordToken) {
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
        return token instanceof SymbolToken && token.getText().equals("##");
    }

    private boolean isWhitespace(@Optional ListItem<Token> item) {
        return item != null && item.get() instanceof WhitespaceToken;
    }
}