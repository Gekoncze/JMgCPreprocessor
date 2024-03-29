package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.tokenizer.test.TokenValidator;

public @Service class MacroValidator {
    private static volatile @Service MacroValidator instance;

    public static @Service MacroValidator getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroValidator();
                    instance.tokenValidator = TokenValidator.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service TokenValidator tokenValidator;

    private MacroValidator() {
    }

    public void assertEquals(@Mandatory Macro expectation, @Mandatory Macro reality) {
        tokenValidator.assertEquals(expectation.getName(), reality.getName());
        tokenValidator.assertEquals(expectation.getParameters(), reality.getParameters());
        tokenValidator.assertEquals(expectation.getTokens(), reality.getTokens());
    }
}
