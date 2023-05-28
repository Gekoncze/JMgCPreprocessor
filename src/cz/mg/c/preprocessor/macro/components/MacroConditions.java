package cz.mg.c.preprocessor.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.exceptions.MacroException;
import cz.mg.collections.list.List;
import cz.mg.collections.pair.Pair;
import cz.mg.tokenizer.entities.Token;

public @Component class MacroConditions {
    private final @Mandatory List<Pair<Token, MacroCondition>> conditions = new List<>();

    public MacroConditions() {
        conditions.addLast(new Pair<>(null, MacroCondition.TRUE));
    }

    public boolean isTrue() {
        return conditions.getLast().getValue() == MacroCondition.TRUE;
    }

    public boolean isFalse() {
        return conditions.getLast().getValue() == MacroCondition.FALSE;
    }

    public boolean isCompleted() {
        return conditions.getLast().getValue() == MacroCondition.COMPLETED;
    }

    public void nest(@Mandatory Token token) {
        conditions.addLast(new Pair<>(token, MacroCondition.TRUE));
    }

    public void unnest(@Mandatory Token token) {
        validateNotRoot(token);
        conditions.removeLast();
        conditions.getLast().setValue(MacroCondition.COMPLETED);
    }

    public void end() {
        conditions.getLast().setValue(MacroCondition.TRUE);
    }

    public void skip() {
        conditions.getLast().setValue(MacroCondition.FALSE);
    }

    public void validateNotRoot(@Mandatory Token token) {
        if (conditions.getLast().getKey() == null) {
            throw new MacroException(
                token.getPosition(),
                "Missing start of macro condition."
            );
        }
    }

    public void validateIsRoot() {
        if (conditions.getLast().getKey() != null) {
            throw new MacroException(
                conditions.getLast().getKey().getPosition(),
                "Missing end of macro condition."
            );
        }
    }
}
