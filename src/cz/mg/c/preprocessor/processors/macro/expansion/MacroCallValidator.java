package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

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

        validateUnsupportedOperations(call);
    }

    private void validateUnsupportedOperations(@Mandatory MacroCall call) {
        for (Token token : call.getMacro().getTokens()) {
            if (token instanceof SpecialToken) {
                if (token.getText().equals("#")) {
                    throw new MacroException(
                        call.getToken().getPosition(),
                        "Macro stringizing operator is not supported yet."
                    );
                }

                if (token.getText().equals("##")) {
                    throw new MacroException(
                        call.getToken().getPosition(),
                        "Macro concatenation operator is not supported yet."
                    );
                }
            }
        }

        if (call.getMacro().getParameters() != null) {
            for (Token parameter : call.getMacro().getParameters()) {
                if (parameter.getText().equals("...")) {
                    throw new MacroException(
                        call.getToken().getPosition(),
                        "Macro vararg parameters are not supported yet."
                    );
                }
            }
        }
    }
}
