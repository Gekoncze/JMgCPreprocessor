package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.CommentProcessorTest;
import cz.mg.c.preprocessor.processors.TokenProcessorTest;
import cz.mg.c.preprocessor.processors.WhitespaceProcessorTest;
import cz.mg.c.preprocessor.processors.backslash.BackslashPositionProcessorTest;
import cz.mg.c.preprocessor.processors.backslash.BackslashProcessorTest;
import cz.mg.c.preprocessor.processors.macro.MacroProcessorTest;
import cz.mg.c.preprocessor.processors.macro.component.MacroBranchesTest;
import cz.mg.c.preprocessor.processors.macro.component.MacroExpanderTest;
import cz.mg.c.preprocessor.processors.macro.directive.*;
import cz.mg.c.preprocessor.processors.macro.directive.special.OperatorConcatenationServiceTest;
import cz.mg.c.preprocessor.processors.macro.directive.special.SpecialMacroParserTest;
import cz.mg.c.preprocessor.processors.macro.directive.special.TokenConcatenationServiceTest;
import cz.mg.c.preprocessor.processors.macro.expansion.*;
import cz.mg.c.preprocessor.processors.macro.expression.ExpressionsTest;
import cz.mg.c.preprocessor.processors.macro.expression.MacroExpressionsTest;

public @Test class AllTests {
    public static void main(String[] args) {
        // cz.mg.c.preprocessor.processors.backslash
        BackslashPositionProcessorTest.main(args);
        BackslashProcessorTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.component
        MacroBranchesTest.main(args);
        MacroExpanderTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.directive.special
        OperatorConcatenationServiceTest.main(args);
        SpecialMacroParserTest.main(args);
        TokenConcatenationServiceTest.main(args);

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

        // cz.mg.c.preprocessor.processors.macro.expansion
        DefinedMacroExpansionServiceTest.main(args);
        FileMacroExpansionServiceTest.main(args);
        LineMacroExpansionServiceTest.main(args);
        MacroCallValidatorTest.main(args);
        MacroExpansionServicesTest.main(args);
        PlainMacroExpansionServiceTest.main(args);

        // cz.mg.c.preprocessor.processors.macro.expression
        ExpressionsTest.main(args);
        MacroExpressionsTest.main(args);

        // cz.mg.c.preprocessor.processors.macro
        MacroProcessorTest.main(args);

        // cz.mg.c.preprocessor.processors
        CommentProcessorTest.main(args);
        TokenProcessorTest.main(args);
        WhitespaceProcessorTest.main(args);

        // cz.mg.c.preprocessor
        PreprocessorTest.main(args);
    }
}
