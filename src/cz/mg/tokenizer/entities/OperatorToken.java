package cz.mg.tokenizer.entities;

import cz.mg.annotations.classes.Entity;

public @Entity class OperatorToken extends Token {
    public OperatorToken() {
    }

    public OperatorToken(String text, int position) {
        super(text, position);
    }
}
