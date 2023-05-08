package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
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
     * Joins operator tokens that are next to each other.
     */
    public void process(@Mandatory List<Token> tokens) {
        if (!tokens.isEmpty()) {
            for (ListItem<Token> item = tokens.getFirstItem(); item.getNextItem() != null; item = item.getNextItem()) {
                join(item);
            }
        }
    }

    private void join(@Mandatory ListItem<Token> item) {
        while (true) {
            Token t = item.get();
            Token nt = item.getNextItem().get();
            if (t instanceof SpecialToken && nt instanceof SpecialToken) {
                char ch = t.getText().charAt(0);
                char nch = nt.getText().charAt(0);
                if (isOperator(ch) && isOperator(nch)) {
                    t.setText(t.getText() + nt.getText());
                    item.getList().removeItem(item.getNextItem());
                    continue;
                }
            }
            break;
        }
    }

    private boolean isOperator(char ch) {
        return ch < OPERATORS.length && OPERATORS[ch];
    }
}
