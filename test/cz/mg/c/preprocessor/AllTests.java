package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.BackslashProcessorTest;
import cz.mg.c.preprocessor.processors.CommentProcessorTest;

public @Test class AllTests {
    public static void main(String[] args) {
        BackslashProcessorTest.main(args);
        CommentProcessorTest.main(args);
    }
}
