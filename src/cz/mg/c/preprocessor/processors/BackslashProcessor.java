package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class BackslashProcessor {
    private static @Optional BackslashProcessor instance;

    public static @Mandatory BackslashProcessor getInstance() {
        if (instance == null) {
            instance = new BackslashProcessor();
        }
        return instance;
    }

    private BackslashProcessor() {
    }

    /**
     * Joins rows that end with a backslash.
     */
    public void process(@Mandatory List<Token> tokens) {
        ListItem<Token> item = tokens.getFirstItem();
        while (item != null) {
            ListItem<Token> nextItem = item.getNextItem();

            if (isBackslash(item)) {
                if (isNewline(item.getNextItem())) {
                    nextItem = item.getNextItem().getNextItem();
                    tokens.removeItem(item.getNextItem());
                    tokens.removeItem(item);
                }
            }

            item = nextItem;
        }
    }

    private boolean isBackslash(@Optional ListItem<Token> item) {
        return is(item, "\\", SpecialToken.class);
    }

    private boolean isNewline(@Optional ListItem<Token> item) {
        return is(item, "\n", WhitespaceToken.class);
    }

    private boolean is(
        @Optional ListItem<Token> item,
        @Mandatory String s,
        @Mandatory Class<? extends Token> tokenClass
    ) {
        return item != null && item.get().getText().equals(s) && tokenClass.isInstance(item.get());
    }
}
