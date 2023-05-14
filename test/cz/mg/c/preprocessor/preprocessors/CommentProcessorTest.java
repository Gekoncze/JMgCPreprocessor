package cz.mg.c.preprocessor.preprocessors;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.CommentProcessor;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.CommentToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Test class CommentProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + CommentProcessorTest.class.getSimpleName() + " ... ");

        CommentProcessorTest test = new CommentProcessorTest();
        test.testProcessingFirst();
        test.testProcessingMiddle();
        test.testProcessingLast();

        System.out.println("OK");
    }

    private void testProcessingFirst() {
        CommentProcessor processor = CommentProcessor.getInstance();
        List<Token> tokens = new List<>(comment("foo bar"), plain("a"), whitespace(" "), plain("bb"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "a", " ", "bb");
    }

    private void testProcessingMiddle() {
        CommentProcessor processor = CommentProcessor.getInstance();
        List<Token> tokens = new List<>(
            plain("foo"),
            comment("="),
            whitespace("\t"),
            plain("bar"),
            documentation(" "),
            plain("69")
        );
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "foo", "\t", "bar", "69");
    }

    private void testProcessingLast() {
        CommentProcessor processor = CommentProcessor.getInstance();
        List<Token> tokens = new List<>(plain("~"), whitespace(" "), documentation("yay\nmay"));
        processor.process(tokens);
        TokenValidator.getInstance().check(tokens, "~", " ");
    }

    private @Mandatory Token plain(@Mandatory String text) {
        return new Token(text, 0);
    }

    private @Mandatory Token comment(@Mandatory String text) {
        return new CommentToken(text, 0);
    }

    private @Mandatory Token documentation(@Mandatory String text) {
        return new CommentToken(text, 0);
    }

    private @Mandatory Token whitespace(@Mandatory String text) {
        return new WhitespaceToken(text, 0);
    }
}
