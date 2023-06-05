package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.macro.expression.ExpressionParserTest;
import cz.mg.c.preprocessor.macro.expression.ExpressionEvaluatorTest;
import cz.mg.c.preprocessor.macro.services.MacroParserTest;
import cz.mg.c.preprocessor.processors.BackslashProcessorTest;
import cz.mg.c.preprocessor.processors.CommentProcessorTest;
import cz.mg.c.preprocessor.processors.WhitespaceProcessorTest;

public @Test class AllTests {
    public static void main(String[] args) {
        // cz.mg.c.preprocessor.macro.expression
        ExpressionEvaluatorTest.main(args);
        ExpressionParserTest.main(args);

        // cz.mg.c.preprocessor.macro.services
        MacroParserTest.main(args);

        // cz.mg.c.preprocessor.processors
        BackslashProcessorTest.main(args);
        CommentProcessorTest.main(args);
        WhitespaceProcessorTest.main(args);

        // cz.mg.c.preprocessor
        PreprocessorTest.main(args);
    }
}
