package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.*;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class OperatorConcatenationServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + OperatorConcatenationServiceTest.class.getSimpleName() + " ... ");

        OperatorConcatenationServiceTest test = new OperatorConcatenationServiceTest();
        test.testConcatenate();

        System.out.println("OK");
    }

    private final @Service OperatorConcatenationService service = OperatorConcatenationService.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testConcatenate() {
        testConcatenate(
            new List<>(),
            new List<>()
        );

        testConcatenate(
            new List<>(new SpecialToken("#", 1)),
            new List<>(new SpecialToken("#", 1))
        );

        testConcatenate(
            new List<>(new NameToken("a", 2)),
            new List<>(new NameToken("a", 2))
        );

        testConcatenate(
            new List<>(new SpecialToken("#", 3), new SpecialToken("#", 4)),
            new List<>(new SpecialToken("##", 3))
        );

        testConcatenate(
            new List<>(new SpecialToken("#", 3), new SpecialToken("#", 5)),
            new List<>(new SpecialToken("#", 3), new SpecialToken("#", 5))
        );

        testConcatenate(
            new List<>(new SpecialToken("#", 3), new NameToken("a", 4)),
            new List<>(new SpecialToken("#", 3), new NameToken("a", 4))
        );

        testConcatenate(
            new List<>(new NumberToken("1", 3), new SpecialToken("#", 4)),
            new List<>(new NumberToken("1", 3), new SpecialToken("#", 4))
        );

        testConcatenate(
            new List<>(new SpecialToken("#", 3), new CommentToken("#", 4)),
            new List<>(new SpecialToken("#", 3), new CommentToken("#", 4))
        );

        testConcatenate(
            new List<>(new DoubleQuoteToken("#", 3), new SpecialToken("#", 4)),
            new List<>(new DoubleQuoteToken("#", 3), new SpecialToken("#", 4))
        );

        testConcatenate(
            new List<>(
                new NumberToken("7", 0), new SpecialToken("#", 3), new SpecialToken("#", 4), new NameToken("a", 10)
            ),
            new List<>(
                new NumberToken("7", 0), new SpecialToken("##", 3), new NameToken("a", 10)
            )
        );
    }

    private void testConcatenate(@Mandatory List<Token> input, @Mandatory List<Token> output) {
        service.concatenate(input);
        validator.assertEquals(output, input);
    }
}
