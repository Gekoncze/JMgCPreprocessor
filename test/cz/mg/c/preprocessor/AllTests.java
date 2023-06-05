package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.backslash.BackslashPositionServiceTest;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionParserTest;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionEvaluatorTest;
import cz.mg.c.preprocessor.processors.macro.services.MacroParserTest;
import cz.mg.c.preprocessor.processors.backslash.BackslashProcessorTest;
import cz.mg.c.preprocessor.processors.CommentProcessorTest;
import cz.mg.c.preprocessor.processors.WhitespaceProcessorTest;

public @Test class AllTests {
    public static void main(String[] args) {
        // cz.mg.c.preprocessor.processors.backslash
        BackslashPositionServiceTest.main(args);
        BackslashProcessorTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.expression
        ExpressionEvaluatorTest.main(args);
        ExpressionParserTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.services
        MacroParserTest.main(args);

        // cz.mg.c.preprocessor.processors
        CommentProcessorTest.main(args);
        WhitespaceProcessorTest.main(args);

        // cz.mg.c.preprocessor
        PreprocessorTest.main(args);
    }
}
