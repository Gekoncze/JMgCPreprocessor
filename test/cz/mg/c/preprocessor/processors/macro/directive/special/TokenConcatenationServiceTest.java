package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WordToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class TokenConcatenationServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + TokenConcatenationServiceTest.class.getSimpleName() + " ... ");

        TokenConcatenationServiceTest test = new TokenConcatenationServiceTest();
        test.testConcatenate();

        System.out.println("OK");
    }

    private final @Service TokenConcatenationService service = TokenConcatenationService.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testConcatenate() {
        testConcatenate(
            new List<>(),
            new List<>()
        );

        testConcatenate(
            new List<>(f.special("##")),
            new List<>(f.special("##"))
        );

        testConcatenate(
            new List<>(f.word("a"), f.special("##")),
            new List<>(f.word("a"), f.special("##"))
        );

        testConcatenate(
            new List<>(f.special("##"), f.word("a")),
            new List<>(f.special("##"), f.word("a"))
        );

        testConcatenate(
            new List<>(new WordToken("foo", 3), new SpecialToken("##", 6), new WordToken("bar", 8)),
            new List<>(new WordToken("foobar", 3))
        );

        testConcatenate(
            new List<>(f.word("f"), f.word("foo"), f.special("##"), f.word("bar"), f.word("b")),
            new List<>(f.word("f"), f.word("foobar"), f.word("b"))
        );

        testConcatenate(
            new List<>(
                f.whitespace(" "),
                f.word("f"),
                f.whitespace(" "),
                f.word("foo"),
                f.whitespace(" "),
                f.special("##"),
                f.whitespace(" "),
                f.word("bar"),
                f.whitespace(" "),
                f.word("b"),
                f.whitespace(" ")
            ),
            new List<>(
                f.whitespace(" "),
                f.word("f"),
                f.whitespace(" "),
                f.word("foobar"),
                f.whitespace(" "),
                f.word("b"),
                f.whitespace(" ")
            )
        );
    }

    private void testConcatenate(@Mandatory List<Token> input, @Mandatory List<Token> output) {
        service.concatenate(input);
        validator.assertEquals(output, input);
    }
}
