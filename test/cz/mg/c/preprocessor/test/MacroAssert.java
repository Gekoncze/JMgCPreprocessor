package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Static;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.token.test.TokenAssert;

public @Static class MacroAssert {
    public static void assertEquals(@Mandatory Macro expectation, @Mandatory Macro reality) {
        TokenAssert.assertEquals(expectation.getName(), reality.getName());
        TokenAssert.assertEquals(expectation.getParameters(), reality.getParameters());
        TokenAssert.assertEquals(expectation.getTokens(), reality.getTokens());
    }
}
