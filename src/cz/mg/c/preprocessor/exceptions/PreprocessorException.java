package cz.mg.c.preprocessor.exceptions;

import cz.mg.annotations.classes.Error;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.exceptions.TraceableException;

public @Error class PreprocessorException extends TraceableException {
    public PreprocessorException(int position, @Mandatory String message) {
        super(position, message);
    }

    public PreprocessorException(int position, @Mandatory String message, @Mandatory Throwable cause) {
        super(position, message, cause);
    }
}
