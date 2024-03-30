package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.token.Token;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class NewlineProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + NewlineProcessorTest.class.getSimpleName() + " ... ");

        NewlineProcessorTest test = new NewlineProcessorTest();
        test.testProcessingNewlines();
        test.testProcessingFirst();
        test.testProcessingMiddle();
        test.testProcessingLast();

        System.out.println("OK");
    }

    private final @Service TokenValidator validator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service NewlineProcessor processor = NewlineProcessor.getInstance();

    private void testProcessingNewlines() {
        List<Token> tokens = new List<>(
            f.word("a"),
            f.whitespace("\n"),
            f.number("11"),
            f.number("69"),
            f.whitespace("\n"),
            f.symbol(";")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(3, lines.count());
        validator.assertEquals(new List<>(f.word("a")), lines.get(0));
        validator.assertEquals(new List<>(f.number("11"), f.number("69")), lines.get(1));
        validator.assertEquals(new List<>(f.symbol(";")), lines.get(2));
    }

    private void testProcessingFirst() {
        List<Token> tokens = new List<>(
            f.whitespace("\n"),
            f.number("3"),
            f.word("foo"),
            f.whitespace(" ")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(2, lines.count());
        validator.assertEquals(new List<>(), lines.get(0));
        validator.assertEquals(new List<>(f.number("3"), f.word("foo"), f.whitespace(" ")), lines.get(1));
    }

    private void testProcessingMiddle() {
        List<Token> tokens = new List<>(
            f.word("foo"),
            f.whitespace("\n"),
            f.word("bar")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(2, lines.count());
        validator.assertEquals(new List<>(f.word("foo")), lines.get(0));
        validator.assertEquals(new List<>(f.word("bar")), lines.get(1));
    }

    private void testProcessingLast() {
        List<Token> tokens = new List<>(
            f.whitespace("\t"),
            f.word("Pony"),
            f.symbol("!"),
            f.whitespace("\n")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(2, lines.count());
        validator.assertEquals(new List<>(f.whitespace("\t"), f.word("Pony"), f.symbol("!")), lines.get(0));
        validator.assertEquals(new List<>(), lines.get(1));
    }
}