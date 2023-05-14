package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.entities.OperatorToken;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class OperatorProcessor {
    private static @Optional OperatorProcessor instance;

    public static @Mandatory OperatorProcessor getInstance() {
        if (instance == null) {
            instance = new OperatorProcessor();
        }
        return instance;
    }

    private final boolean[] OPERATORS = new boolean[128];

    private OperatorProcessor() {
        OPERATORS['+'] = true;
        OPERATORS['-'] = true;
        OPERATORS['*'] = true;
        OPERATORS['/'] = true;
        OPERATORS['%'] = true;
        OPERATORS['<'] = true;
        OPERATORS['>'] = true;
        OPERATORS['='] = true;
        OPERATORS['~'] = true;
        OPERATORS['!'] = true;
        OPERATORS['^'] = true;
        OPERATORS['&'] = true;
        OPERATORS['|'] = true;
    }

    /**
     * Transforms special tokens with operator to operator tokens.
     * Joins operator tokens that are next to each other.
     */
    public void process(@Mandatory List<Token> tokens) {
        transform(tokens);
        join(tokens);
    }

    private void transform(@Mandatory List<Token> tokens) {
        for (ListItem<Token> item = tokens.getFirstItem(); item != null; item = tokens.getFirstItem().getNextItem()) {
            Token token = item.get();
            if (isOperator(token)) {
                item.set(
                    new OperatorToken(
                        token.getText(),
                        token.getPosition()
                    )
                );
            }
        }
    }

    private void join(@Mandatory List<Token> tokens) {
        for (ListItem<Token> item = tokens.getFirstItem(); item.getNextItem() != null; item = item.getNextItem()) {
            join(item);
        }
    }

    private void join(@Mandatory ListItem<Token> item) {
        while (true) {
            Token t = item.get();
            Token nt = item.getNextItem().get();

            if (t instanceof OperatorToken && nt instanceof OperatorToken) {
                t.setText(t.getText() + nt.getText());
                item.getList().removeItem(item.getNextItem());
                continue;
            }

            break;
        }
    }

    private boolean isOperator(@Mandatory Token token) {
        return token instanceof SpecialToken
            && isOperator(token.getText().charAt(0));
    }

    private boolean isOperator(char ch) {
        return ch < OPERATORS.length && OPERATORS[ch];
    }
}
