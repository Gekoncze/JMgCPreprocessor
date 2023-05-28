package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.utilities.MacroException;
import cz.mg.c.preprocessor.macro.utilities.MacroExpansion;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroExpansionValidator {
    private static volatile @Service MacroExpansionValidator instance;

    public static @Service MacroExpansionValidator getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroExpansionValidator();
                }
            }
        }
        return instance;
    }

    private MacroExpansionValidator() {
    }

    @SuppressWarnings("ConstantConditions")
    public void validate(@Mandatory MacroExpansion expansion) {
        boolean actualNull = expansion.getArguments() == null;
        boolean expectedNull = expansion.getMacro().getParameters() == null;

        if (actualNull && !expectedNull) {
            throw new MacroException(
                expansion.getToken().getPosition(),
                "Macro expansion with null arguments where non-null expected by macro definition."
            );
        }

        if (!actualNull && expectedNull) {
            throw new MacroException(
                expansion.getToken().getPosition(),
                "Macro expansion with non-null arguments where null expected by macro definition."
            );
        }

        if (!actualNull && !expectedNull) {
            int actualCount = expansion.getArguments().count();
            int expectedCount = expansion.getMacro().getParameters().count();

            if (actualCount != expectedCount) {
                throw new MacroException(
                    expansion.getToken().getPosition(),
                    "Wrong number of arguments for macro expansion." +
                        " Expected " + actualCount + ", but got " + expectedCount + "."
                );
            }

            for (List<Token> arguments : expansion.getArguments()) {
                if (arguments.isEmpty()) {
                    throw new MacroException(
                        expansion.getToken().getPosition(),
                        "Unexpected empty argument list in macro expansion."
                    );
                }
            }
        }
    }
}
