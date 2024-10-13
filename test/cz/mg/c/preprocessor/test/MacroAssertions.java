package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.tokenizer.test.TokenAssertions;

public @Service class MacroAssertions {
    private static volatile @Service MacroAssertions instance;

    public static @Service MacroAssertions getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroAssertions();
                    instance.tokenAssertions = TokenAssertions.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service TokenAssertions tokenAssertions;

    private MacroAssertions() {
    }

    public void assertEquals(@Mandatory Macro expectation, @Mandatory Macro reality) {
        tokenAssertions.assertEquals(expectation.getName(), reality.getName());
        tokenAssertions.assertEquals(expectation.getParameters(), reality.getParameters());
        tokenAssertions.assertEquals(expectation.getTokens(), reality.getTokens());
    }
}
