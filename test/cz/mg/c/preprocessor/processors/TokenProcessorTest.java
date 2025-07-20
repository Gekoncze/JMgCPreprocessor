package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.tokenizer.CTokenizer;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.token.Token;
import cz.mg.token.tokens.NumberToken;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WhitespaceToken;
import cz.mg.token.tokens.quote.SingleQuoteToken;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.token.test.TokenAssertions;

public @Test class TokenProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + TokenProcessorTest.class.getSimpleName() + " ... ");

        TokenProcessorTest test = new TokenProcessorTest();
        test.testProcess();
        test.testProcessException();

        System.out.println("OK");
    }

    private final @Service TokenProcessor tokenProcessor = TokenProcessor.getInstance();
    private final @Service TokenAssertions assertions = TokenAssertions.getInstance();
    private final @Mandatory CTokenizer tokenizer = new CTokenizer();

    private void testProcess() {
        List<Token> actualTokens = tokenProcessor.process("1 + 1\\\n = '2'", tokenizer);
        List<Token> expectedTokens = new List<>(
            new NumberToken("1", 0),
            new WhitespaceToken(" ", 1),
            new SymbolToken("+", 2),
            new WhitespaceToken(" ", 3),
            new NumberToken("1", 4),
            new WhitespaceToken(" ", 7),
            new SymbolToken("=", 8),
            new WhitespaceToken(" ", 9),
            new SingleQuoteToken("2", 10)
        );
        assertions.assertEquals(expectedTokens, actualTokens);
    }

    private void testProcessException() {
        TraceableException exception = Assert.assertThatCode(() -> {
            tokenProcessor.process("1 + 1\\\n = '2", tokenizer);
        }).throwsException(TraceableException.class);
        Assert.assertEquals(10, exception.getPosition());
    }
}