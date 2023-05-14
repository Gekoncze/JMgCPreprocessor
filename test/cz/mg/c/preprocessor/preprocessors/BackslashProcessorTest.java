package cz.mg.c.preprocessor.preprocessors;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.BackslashProcessor;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Test class BackslashProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + BackslashProcessorTest.class.getSimpleName() + " ... ");

        BackslashProcessorTest test = new BackslashProcessorTest();
        test.testProcessingFirst();
        test.testProcessingMiddle();
        test.testProcessingLast();
        test.testFakeBackslash();
        test.testMissortedBackslash();
        test.testOther();

        System.out.println("OK");
    }

    private final TokenValidator validator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testProcessingFirst() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(f.special("\\"), f.whitespace("\n"), f.plain("6"), f.plain("9"));
        processor.process(tokens);
        validator.check(tokens, "6", "9");
    }

    private void testProcessingMiddle() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(
            f.plain("foo"),
            f.special("\\"),
            f.whitespace("\n"),
            f.plain("bar"),
            f.special("\\"),
            f.whitespace("\n"),
            f.plain("69")
        );
        processor.process(tokens);
        validator.check(tokens, "foo", "bar", "69");
    }

    private void testProcessingLast() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(f.plain("6"), f.plain("9"), f.special("\\"), f.whitespace("\n"));
        processor.process(tokens);
        validator.check(tokens, "6", "9");
    }

    private void testFakeBackslash() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(f.plain("6"), f.plain("\\"), f.plain("\n"), f.plain("9"));
        processor.process(tokens);
        validator.check(tokens, "6", "\\", "\n", "9");
    }

    private void testMissortedBackslash() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(f.plain("6"), f.whitespace("\n"), f.special("\\"), f.plain("9"));
        processor.process(tokens);
        validator.check(tokens, "6", "\n", "\\", "9");
    }

    private void testOther() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(f.plain("6"), f.special("a"), f.whitespace("b"), f.plain("9"));
        processor.process(tokens);
        validator.check(tokens, "6", "a", "b", "9");
    }
}
