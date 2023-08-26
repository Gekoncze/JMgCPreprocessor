package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.collections.list.ListItem;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class OperatorConcatenationService {
    private static volatile @Service OperatorConcatenationService instance;

    public static @Service OperatorConcatenationService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new OperatorConcatenationService();
                }
            }
        }
        return instance;
    }

    private OperatorConcatenationService() {
    }

    public void concatenate(@Mandatory List<Token> tokens) {
        for (ListItem<Token> item = tokens.getFirstItem(); item != null; item = item.getNextItem()) {
            if (item.getNextItem() != null) {
                Token left = item.get();
                Token right = item.getNextItem().get();
                if (isSingleNumberSign(left) && isSingleNumberSign(right)) {
                    if (left.getPosition() == (right.getPosition() - 1)) {
                        item.removeNext();
                        item.set(extend(item.get()));
                    }
                }
            }
        }
    }

    private @Mandatory Token extend(@Mandatory Token token) {
        return new SpecialToken("##", token.getPosition());
    }

    private boolean isSingleNumberSign(@Mandatory Token token) {
        return token instanceof SpecialToken && token.getText().equals("#");
    }
}
