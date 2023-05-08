package cz.mg.c.preprocessor.macro.utilities;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.utilities.PreprocessorException;

public @Utility class MacroException extends PreprocessorException {
    public MacroException(int position, @Mandatory String message) {
        super(position, message);
    }

    public MacroException(int position, @Mandatory String message, @Mandatory Throwable cause) {
        super(position, message, cause);
    }
}
