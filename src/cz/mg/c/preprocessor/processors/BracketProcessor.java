package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.entities.OperatorToken;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class BracketProcessor {
    private static @Optional BracketProcessor instance;

    public static @Mandatory BracketProcessor getInstance() {
        if (instance == null) {
            instance = new BracketProcessor();
        }
        return instance;
    }

    private final boolean[] BRACKETS = new boolean[128];

    private BracketProcessor() {
        BRACKETS['('] = true;
        BRACKETS[')'] = true;
        BRACKETS['['] = true;
        BRACKETS[']'] = true;
        BRACKETS['{'] = true;
        BRACKETS['}'] = true;
    }

    /**
     * Transforms special tokens with bracket to bracket tokens.
     */
    public void process(@Mandatory List<Token> tokens) {
        transform(tokens);
    }

    private void transform(@Mandatory List<Token> tokens) {
        for (ListItem<Token> item = tokens.getFirstItem(); item != null; item = tokens.getFirstItem().getNextItem()) {
            Token token = item.get();
            if (isBracket(token)) {
                item.set(
                    new OperatorToken(
                        token.getText(),
                        token.getPosition()
                    )
                );
            }
        }
    }

    private boolean isBracket(@Mandatory Token token) {
        return token instanceof SpecialToken
            && isBracket(token.getText().charAt(0));
    }

    private boolean isBracket(char ch) {
        return ch < BRACKETS.length && BRACKETS[ch];
    }
}
