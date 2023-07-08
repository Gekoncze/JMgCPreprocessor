package cz.mg.c.preprocessor.processors.macro.exceptions;

import cz.mg.annotations.classes.Error;
import cz.mg.annotations.requirement.Mandatory;

public @Error class ErrorException extends MacroException {
    public ErrorException(int position, @Mandatory String message) {
        super(position, message);
    }

    public ErrorException(int position, @Mandatory String message, @Mandatory Throwable cause) {
        super(position, message, cause);
    }
}
