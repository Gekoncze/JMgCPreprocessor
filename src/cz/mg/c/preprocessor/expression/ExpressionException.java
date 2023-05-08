package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.utilities.PreprocessorException;

public @Utility class ExpressionException extends PreprocessorException {
    public ExpressionException(int position, @Mandatory String message) {
        super(position, message);
    }

    public ExpressionException(int position, @Mandatory String message, @Mandatory Exception cause) {
        super(position, message, cause);
    }
}
