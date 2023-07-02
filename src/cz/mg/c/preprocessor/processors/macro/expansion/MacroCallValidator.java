package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroCallValidator {
    private static volatile @Service MacroCallValidator instance;

    public static @Service MacroCallValidator getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroCallValidator();
                }
            }
        }
        return instance;
    }

    private MacroCallValidator() {
    }

    @SuppressWarnings("ConstantConditions")
    public void validate(@Mandatory MacroCall call) {
        boolean actualNull = call.getArguments() == null;
        boolean expectedNull = call.getMacro().getParameters() == null;

        if (actualNull && !expectedNull) {
            throw new MacroException(
                call.getToken().getPosition(),
                "Macro call with null arguments where non-null expected by macro definition."
            );
        }

        if (!actualNull && expectedNull) {
            throw new MacroException(
                call.getToken().getPosition(),
                "Macro call with non-null arguments where null expected by macro definition."
            );
        }

        if (!actualNull && !expectedNull) {
            int actualCount = call.getArguments().count();
            int expectedCount = call.getMacro().getParameters().count();

            if (actualCount != expectedCount) {
                throw new MacroException(
                    call.getToken().getPosition(),
                    "Wrong number of arguments for macro call." +
                        " Expected " + expectedCount + ", but got " + actualCount + "."
                );
            }

            for (List<Token> arguments : call.getArguments()) {
                if (arguments.isEmpty()) {
                    throw new MacroException(
                        call.getToken().getPosition(),
                        "Unexpected empty argument list in macro call."
                    );
                }
            }
        }
    }
}
