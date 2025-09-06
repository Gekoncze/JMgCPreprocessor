package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.test.TokenFactory;

public @Test class CommentProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + CommentProcessorTest.class.getSimpleName() + " ... ");

        CommentProcessorTest test = new CommentProcessorTest();
        test.testProcessing();

        System.out.println("OK");
    }

    private final CommentProcessor processor = CommentProcessor.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testProcessing() {
        testProcessing(
            new List<>(),
            new List<>()
        );

        testProcessing(
            new List<>(f.comment("")),
            new List<>()
        );

        testProcessing(
            new List<>(f.word("a")),
            new List<>(f.word("a"))
        );

        testProcessing(
            new List<>(
                f.comment("foo bar"),
                f.token("a"),
                f.whitespace(" "),
                f.token("bb")
            ),
            new List<>(
                f.token("a"),
                f.whitespace(" "),
                f.token("bb")
            )
        );

        testProcessing(
            new List<>(
                f.token("foo"),
                f.comment("="),
                f.whitespace("\t"),
                f.token("bar"),
                f.comment(" "),
                f.number("69")
            ),
            new List<>(
                f.token("foo"),
                f.whitespace("\t"),
                f.token("bar"),
                f.number("69")
            )
        );

        testProcessing(
            new List<>(
                f.token("~"),
                f.whitespace(" "),
                f.comment("yay\nmay")
            ),
            new List<>(
                f.token("~"),
                f.whitespace(" ")
            )
        );
    }

    private void testProcessing(@Mandatory List<Token> tokens, @Mandatory List<Token> result) {
        processor.process(tokens);
        TokenAssert.assertEquals(result, tokens);
    }
}