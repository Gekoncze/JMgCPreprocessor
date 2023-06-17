package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Test class CommentProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + CommentProcessorTest.class.getSimpleName() + " ... ");

        CommentProcessorTest test = new CommentProcessorTest();
        test.testProcessingFirst();
        test.testProcessingMiddle();
        test.testProcessingLast();

        System.out.println("OK");
    }

    private final TokenValidator validator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();
    private final CommentProcessor processor = CommentProcessor.getInstance();

    private void testProcessingFirst() {
        List<Token> tokens = new List<>(f.comment("foo bar"), f.plain("a"), f.whitespace(" "), f.plain("bb"));
        processor.process(tokens);
        validator.assertEquals(new List<>(f.plain("a"), f.whitespace(" "), f.plain("bb")), tokens);
    }

    private void testProcessingMiddle() {
        List<Token> tokens = new List<>(
            f.plain("foo"),
            f.comment("="),
            f.whitespace("\t"),
            f.plain("bar"),
            f.comment(" "),
            f.number("69")
        );
        processor.process(tokens);
        validator.assertEquals(new List<>(
            f.plain("foo"),
            f.whitespace("\t"),
            f.plain("bar"),
            f.number("69")
        ), tokens);
    }

    private void testProcessingLast() {
        List<Token> tokens = new List<>(f.plain("~"), f.whitespace(" "), f.comment("yay\nmay"));
        processor.process(tokens);
        validator.assertEquals(new List<>(f.plain("~"), f.whitespace(" ")), tokens);
    }
}
