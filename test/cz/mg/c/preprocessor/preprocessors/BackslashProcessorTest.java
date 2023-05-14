package cz.mg.c.preprocessor.preprocessors;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.BackslashProcessor;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

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

    private void testProcessingFirst() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(special("\\"), whitespace("\n"), plain("6"), plain("9"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "6", "9");
    }

    private void testProcessingMiddle() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(
            plain("foo"),
            special("\\"),
            whitespace("\n"),
            plain("bar"),
            special("\\"),
            whitespace("\n"),
            plain("69")
        );
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "foo", "bar", "69");
    }

    private void testProcessingLast() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(plain("6"), plain("9"), special("\\"), whitespace("\n"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "6", "9");
    }

    private void testFakeBackslash() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(plain("6"), plain("\\"), plain("\n"), plain("9"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "6", "\\", "\n", "9");
    }

    private void testMissortedBackslash() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(plain("6"), whitespace("\n"), special("\\"), plain("9"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "6", "\n", "\\", "9");
    }

    private void testOther() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        List<Token> tokens = new List<>(plain("6"), special("a"), whitespace("b"), plain("9"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "6", "a", "b", "9");
    }

    private @Mandatory Token plain(@Mandatory String text) {
        return new Token(text, 0);
    }

    private @Mandatory Token special(@Mandatory String text) {
        return new SpecialToken(text, 0);
    }

    private @Mandatory Token whitespace(@Mandatory String text) {
        return new WhitespaceToken(text, 0);
    }
}
