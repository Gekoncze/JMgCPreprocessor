package cz.mg.c.preprocessor.utilities;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.utilities.CodeException;

public @Utility class PreprocessorException extends CodeException {
    public PreprocessorException(int position, @Mandatory String message) {
        super(position, message);
    }

    public PreprocessorException(int position, @Mandatory String message, @Mandatory Throwable cause) {
        super(position, message, cause);
    }
}
