package cz.mg.tokenizer.entities;

import cz.mg.annotations.classes.Entity;

public @Entity class BracketToken extends Token {
    public BracketToken() {
    }

    public BracketToken(String text, int position) {
        super(text, position);
    }
}
