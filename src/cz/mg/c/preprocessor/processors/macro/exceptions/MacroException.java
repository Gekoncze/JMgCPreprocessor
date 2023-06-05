package cz.mg.c.preprocessor.processors.macro.exceptions;

import cz.mg.annotations.classes.Error;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;

public @Error class MacroException extends PreprocessorException {
    public MacroException(int position, @Mandatory String message) {
        super(position, message);
    }

    public MacroException(int position, @Mandatory String message, @Mandatory Throwable cause) {
        super(position, message, cause);
    }
}
