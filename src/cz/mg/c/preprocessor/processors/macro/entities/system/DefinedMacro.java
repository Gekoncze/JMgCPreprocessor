package cz.mg.c.preprocessor.processors.macro.entities.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Entity class DefinedMacro extends Macro implements SystemMacro {
    public static final @Mandatory String NAME = "defined";

    public DefinedMacro() {
        super(
            new WordToken(NAME, -1),
            new List<>(new WordToken("x", -1)),
            new List<>()
        );
    }
}
