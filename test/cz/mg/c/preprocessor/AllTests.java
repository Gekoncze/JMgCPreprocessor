package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.CommentProcessorTest;
import cz.mg.c.preprocessor.processors.WhitespaceProcessorTest;
import cz.mg.c.preprocessor.processors.backslash.BackslashPositionServiceTest;
import cz.mg.c.preprocessor.processors.backslash.BackslashProcessorTest;
import cz.mg.c.preprocessor.processors.macro.component.MacroExpanderTest;
import cz.mg.c.preprocessor.processors.macro.directive.*;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionsTest;
import cz.mg.c.preprocessor.processors.macro.directive.ExpressionParserTest;
import cz.mg.c.preprocessor.processors.macro.directive.MacroParserTest;

public @Test class AllTests {
    public static void main(String[] args) {
        // cz.mg.c.preprocessor.processors.backslash
        BackslashPositionServiceTest.main(args);
        BackslashProcessorTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.component
        MacroExpanderTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.directive
        DefineDirectiveParserTest.main(args);
        DirectiveParsersTest.main(args);
        ElifDirectiveParserTest.main(args);
        ElseDirectiveParserTest.main(args);
        EndifDirectiveParserTest.main(args);
        ErrorDirectiveParserTest.main(args);
        ExpressionParserTest.main(args);
        IfdefDirectiveParserTest.main(args);
        IfDirectiveParserTest.main(args);
        IfndefDirectiveParserTest.main(args);
        IncludeDirectiveParserTest.main(args);
        MacroParserTest.main(args);
        UndefDirectiveParserTest.main(args);
        WarningDirectiveParserTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.expression
        ExpressionsTest.main(args);

        // cz.mg.c.preprocessor.processors
        CommentProcessorTest.main(args);
        WhitespaceProcessorTest.main(args);

        // cz.mg.c.preprocessor
        PreprocessorTest.main(args);
    }
}
