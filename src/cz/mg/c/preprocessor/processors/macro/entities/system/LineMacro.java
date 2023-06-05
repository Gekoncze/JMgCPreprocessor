package cz.mg.c.preprocessor.processors.macro.entities.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.tokenizer.entities.tokens.NameToken;

public @Entity class LineMacro extends Macro implements SystemMacro {
    public static final @Mandatory String NAME = "__LINE__";

    public LineMacro() {
        super(
            new NameToken(NAME, -1),
            null,
            null
        );
    }
}
