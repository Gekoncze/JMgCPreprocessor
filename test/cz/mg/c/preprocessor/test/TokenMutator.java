package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;

public @Service class TokenMutator {
    private static volatile @Service TokenMutator instance;

    public static @Service TokenMutator getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new TokenMutator();
                }
            }
        }
        return instance;
    }

    private TokenMutator() {
    }

    /**
     * Triggers consumer with various combinations of given tokens mutated.
     * Mutated tokens are replaced with tokens created by factory with the same text and position.
     * Only tokens specified by target positions are mutated.
     * First consumer is triggered with original tokens and is expected to pass.
     * Then consumer is triggered with various combinations of mutated tokens and is expected to not pass.
     */
    public <R> void mutate(
        @Mandatory List<Token> tokens,
        @Mandatory List<Integer> targets,
        @Mandatory Factory factory,
        @Mandatory Consumer<R> consumer,
        @Mandatory Validator<R> validator
    ) {
        validator.validate(consumer.consume(tokens));

        int i = -1;
        for (ListItem<Token> item = tokens.getFirstItem(); item != null; item = item.getNextItem()) {
            i++;
            if (targets.contains(i)) {
                mutate(i, item, factory, consumer);
            }
        }
    }

    private <R> void mutate(
        int i,
        @Mandatory ListItem<Token> item,
        @Mandatory Factory factory,
        @Mandatory Consumer<R> consumer
    ) {
        Assert
            .assertThatCode(() -> {
                Token originalToken = item.get();
                Token mutatedToken = factory.create();
                mutatedToken.setText(originalToken.getText());
                mutatedToken.setPosition(originalToken.getPosition());
                try {
                    item.set(mutatedToken);
                    consumer.consume(item.getList());
                } finally {
                    item.set(originalToken);
                }
            })
            .withMessage("Mutated token at " + i + " passed.")
            .throwsException();
    }

    public <R> void mutate(
        @Mandatory List<Token> tokens,
        @Mandatory List<Integer> targets,
        @Mandatory Consumer<R> consumer,
        @Mandatory Validator<R> validator
    ) {
        mutate(tokens, targets, Token::new, consumer, validator);
    }

    public interface Factory {
        @Mandatory Token create();
    }

    public interface Consumer<R> {
        R consume(@Mandatory List<Token> tokens);
    }

    public interface Validator<R> {
        void validate(@Mandatory R result);
    }
}
