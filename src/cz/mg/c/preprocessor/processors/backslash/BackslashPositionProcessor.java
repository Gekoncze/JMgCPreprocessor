package cz.mg.c.preprocessor.processors.backslash;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.entities.Token;

public @Service class BackslashPositionProcessor {
    private static volatile @Service BackslashPositionProcessor instance;

    public static @Service BackslashPositionProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new BackslashPositionProcessor();
                }
            }
        }
        return instance;
    }

    private BackslashPositionProcessor() {
    }

    public void process(
        @Mandatory String originalContent,
        @Mandatory String backslashedContent,
        @Mandatory List<Token> tokens
    ) {
        int originalPosition = 0;
        int backslashedPosition = 0;
        ListItem<Token> item = tokens.getFirstItem();

        while (
            item != null &&
            originalPosition < originalContent.length() &&
            backslashedPosition < backslashedContent.length()
        ) {
            char originalCharacter = originalContent.charAt(originalPosition);
            char backslashedCharacter = backslashedContent.charAt(backslashedPosition);

            while (originalCharacter != backslashedCharacter) {
                originalPosition++;
                originalCharacter = originalContent.charAt(originalPosition);
            }

            if (item.get().getPosition() == backslashedPosition) {
                item.get().setPosition(originalPosition);
                item = item.getNextItem();
            }

            originalPosition++;
            backslashedPosition++;
        }

        if (item != null) {
            throw new IllegalStateException("Token position out of bounds.");
        }
    }
}
