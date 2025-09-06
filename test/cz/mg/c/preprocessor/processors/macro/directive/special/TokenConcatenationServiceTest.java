package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.test.TokenFactory;

public @Test class TokenConcatenationServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + TokenConcatenationServiceTest.class.getSimpleName() + " ... ");

        TokenConcatenationServiceTest test = new TokenConcatenationServiceTest();
        test.testConcatenate();

        System.out.println("OK");
    }

    private final @Service TokenConcatenationService service = TokenConcatenationService.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testConcatenate() {
        testConcatenate(
            new List<>(),
            new List<>()
        );

        testConcatenate(
            new List<>(f.symbol("##")),
            new List<>(f.symbol("##"))
        );

        testConcatenate(
            new List<>(f.word("a"), f.symbol("##")),
            new List<>(f.word("a"), f.symbol("##"))
        );

        testConcatenate(
            new List<>(f.symbol("##"), f.word("a")),
            new List<>(f.symbol("##"), f.word("a"))
        );

        testConcatenate(
            new List<>(new WordToken("foo", 3), new SymbolToken("##", 6), new WordToken("bar", 8)),
            new List<>(new WordToken("foobar", 3))
        );

        testConcatenate(
            new List<>(f.word("f"), f.word("foo"), f.symbol("##"), f.word("bar"), f.word("b")),
            new List<>(f.word("f"), f.word("foobar"), f.word("b"))
        );

        testConcatenate(
            new List<>(
                f.whitespace(" "),
                f.word("f"),
                f.whitespace(" "),
                f.word("foo"),
                f.whitespace(" "),
                f.symbol("##"),
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
        TokenAssert.assertEquals(output, input);
    }
}