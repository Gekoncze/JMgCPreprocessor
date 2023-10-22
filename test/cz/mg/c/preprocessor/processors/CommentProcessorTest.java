package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class CommentProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + CommentProcessorTest.class.getSimpleName() + " ... ");

        CommentProcessorTest test = new CommentProcessorTest();
        test.testProcessing();

        System.out.println("OK");
    }

    private final TokenValidator validator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();
    private final CommentProcessor processor = CommentProcessor.getInstance();

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
            new List<>(f.name("a")),
            new List<>(f.name("a"))
        );

        testProcessing(
            new List<>(
                f.comment("foo bar"),
                f.plain("a"),
                f.whitespace(" "),
                f.plain("bb")
            ),
            new List<>(
                f.plain("a"),
                f.whitespace(" "),
                f.plain("bb")
            )
        );

        testProcessing(
            new List<>(
                f.plain("foo"),
                f.comment("="),
                f.whitespace("\t"),
                f.plain("bar"),
                f.comment(" "),
                f.number("69")
            ),
            new List<>(
                f.plain("foo"),
                f.whitespace("\t"),
                f.plain("bar"),
                f.number("69")
            )
        );

        testProcessing(
            new List<>(
                f.plain("~"),
                f.whitespace(" "),
                f.comment("yay\nmay")
            ),
            new List<>(
                f.plain("~"),
                f.whitespace(" ")
            )
        );
    }

    private void testProcessing(@Mandatory List<Token> tokens, @Mandatory List<Token> result) {
        processor.process(tokens);
        validator.assertEquals(result, tokens);
    }
}
