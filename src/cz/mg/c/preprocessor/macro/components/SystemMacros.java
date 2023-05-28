package cz.mg.c.preprocessor.macro.components;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.entities.Macro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.tokens.NameToken;

public @Static class SystemMacros {
    public static final @Mandatory Macro DEFINED = new Macro(
        new NameToken("defined", -1),
        new List<>(new NameToken("x", -1)),
        null
    );

    public static final @Mandatory Macro __FILE__ = new Macro(
        new NameToken("__FILE__", -1),
        null,
        null
    );

    public static final @Mandatory Macro __LINE__ = new Macro(
        new NameToken("__LINE__", -1),
        null,
        null
    );
}
