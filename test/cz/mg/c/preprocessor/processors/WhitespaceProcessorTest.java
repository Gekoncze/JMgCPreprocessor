package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;

public @Test class WhitespaceProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + WhitespaceProcessorTest.class.getSimpleName() + " ... ");

        WhitespaceProcessorTest test = new WhitespaceProcessorTest();
        test.testProcessingWhitespaces();
        test.testProcessingNewlines();
        test.testProcessingFirst();
        test.testProcessingMiddle();
        test.testProcessingLast();

        System.out.println("OK");
    }

    private final TokenValidator validator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();
    private final WhitespaceProcessor processor = WhitespaceProcessor.getInstance();

    private void testProcessingWhitespaces() {
        List<Token> tokens = new List<>(
            f.name("a"),
            f.whitespace(" "),
            f.number("11"),
            f.whitespace("\t"),
            f.separator(";")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(1, lines.count());
        validator.check(lines.get(0), "a", "11", ";");
    }

    private void testProcessingNewlines() {
        List<Token> tokens = new List<>(
            f.name("a"),
            f.whitespace("\n"),
            f.number("11"),
            f.number("69"),
            f.whitespace("\n"),
            f.separator(";")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(3, lines.count());
        validator.check(lines.get(0), "a");
        validator.check(lines.get(1), "11", "69");
        validator.check(lines.get(2), ";");
    }

    private void testProcessingFirst() {
        List<Token> tokens = new List<>(
            f.whitespace("\n"),
            f.number("3"),
            f.name("foo"),
            f.whitespace(" ")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(2, lines.count());
        validator.check(lines.get(0));
        validator.check(lines.get(1), "3", "foo");
    }

    private void testProcessingMiddle() {
        List<Token> tokens = new List<>(
            f.name("foo"),
            f.whitespace("\n"),
            f.whitespace(" "),
            f.whitespace("\t"),
            f.name("bar")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(2, lines.count());
        validator.check(lines.get(0), "foo");
        validator.check(lines.get(1), "bar");
    }

    private void testProcessingLast() {
        List<Token> tokens = new List<>(
            f.whitespace("\t"),
            f.name("Pony"),
            f.operator("!"),
            f.whitespace("\n")
        );
        List<List<Token>> lines = processor.process(tokens);
        Assert.assertEquals(2, lines.count());
        validator.check(lines.get(0), "Pony", "!");
        validator.check(lines.get(1));
    }
}
