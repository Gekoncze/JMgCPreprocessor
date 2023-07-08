package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.entities.tokens.OperatorToken;
import cz.mg.tokenizer.entities.tokens.SingleQuoteToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;
import cz.mg.tokenizer.exceptions.CodeException;

public @Test class TokenProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + TokenProcessorTest.class.getSimpleName() + " ... ");

        TokenProcessorTest test = new TokenProcessorTest();
        test.testProcess();
        test.testProcessException();

        System.out.println("OK");
    }

    private final @Service TokenProcessor tokenProcessor = TokenProcessor.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testProcess() {
        List<Token> actualTokens = tokenProcessor.process("1 + 1\\\n = '2'");
        List<Token> expectedTokens = new List<>(
            new NumberToken("1", 0),
            new WhitespaceToken(" ", 1),
            new OperatorToken("+", 2),
            new WhitespaceToken(" ", 3),
            new NumberToken("1", 4),
            new WhitespaceToken(" ", 7),
            new OperatorToken("=", 8),
            new WhitespaceToken(" ", 9),
            new SingleQuoteToken("2", 10)
        );
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testProcessException() {
        CodeException exception = Assert.assertThatCode(() -> {
            tokenProcessor.process("1 + 1\\\n = '2");
        }).throwsException(CodeException.class);
        Assert.assertEquals(10, exception.getPosition());
    }
}
