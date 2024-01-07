package cz.mg.c.entities.macro.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Entity class LineMacro extends Macro implements SystemMacro {
    public static final @Mandatory String NAME = "__LINE__";

    public LineMacro() {
        super(
            new WordToken(NAME, -1),
            null,
            new List<>()
        );
    }
}
