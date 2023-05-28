package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.expression.ExpressionParserTest;
import cz.mg.c.preprocessor.processors.BackslashProcessorTest;
import cz.mg.c.preprocessor.processors.CommentProcessorTest;

public @Test class AllTests {
    public static void main(String[] args) {
        // cz.mg.c.preprocessor.expression
        ExpressionParserTest.main(args);

        // cz.mg.c.preprocessor.processors
        BackslashProcessorTest.main(args);
        CommentProcessorTest.main(args);
    }
}
