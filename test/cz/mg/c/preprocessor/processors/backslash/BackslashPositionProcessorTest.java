package cz.mg.c.preprocessor.processors.backslash;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssertions;

public @Test class BackslashPositionProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + BackslashPositionProcessorTest.class.getSimpleName() + " ... ");

        BackslashPositionProcessorTest test = new BackslashPositionProcessorTest();
        test.testProcessing();

        System.out.println("OK");
    }

    private final @Service BackslashPositionProcessor backslashPositionProcessor = BackslashPositionProcessor.getInstance();
    private final @Service BackslashProcessor backslashProcessor = BackslashProcessor.getInstance();
    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();

    private void testProcessing() {
        String originalContent =
            "abcdef" + "\\" + "\n" +
            "123" + "\\" + "56" + "\\" + "\n" +
            "xyz" + "\\" + "\n" +
            "\\" + "\n" +
            "789" + "\n" +
            "vw";

        String backslashedContent = backslashProcessor.process(originalContent);

        List<Token> tokens = new List<>(
            createToken(backslashedContent, 'a'),
            createToken(backslashedContent, 'b'),
            createToken(backslashedContent, 'f'),
            createToken(backslashedContent, '1'),
            createToken(backslashedContent, '2'),
            createToken(backslashedContent, '5'),
            createToken(backslashedContent, '6'),
            createToken(backslashedContent, 'x'),
            createToken(backslashedContent, 'y'),
            createToken(backslashedContent, 'z'),
            createToken(backslashedContent, '7'),
            createToken(backslashedContent, '8'),
            createToken(backslashedContent, '9'),
            createToken(backslashedContent, 'v'),
            createToken(backslashedContent, 'w')
        );

        List<Token> expectedTokens = new List<>(
            createToken(originalContent, 'a'),
            createToken(originalContent, 'b'),
            createToken(originalContent, 'f'),
            createToken(originalContent, '1'),
            createToken(originalContent, '2'),
            createToken(originalContent, '5'),
            createToken(originalContent, '6'),
            createToken(originalContent, 'x'),
            createToken(originalContent, 'y'),
            createToken(originalContent, 'z'),
            createToken(originalContent, '7'),
            createToken(originalContent, '8'),
            createToken(originalContent, '9'),
            createToken(originalContent, 'v'),
            createToken(originalContent, 'w')
        );

        tokenAssertions.assertNotEquals(expectedTokens, tokens);

        backslashPositionProcessor.process(originalContent, backslashedContent, tokens);

        tokenAssertions.assertEquals(expectedTokens, tokens);
    }

    private @Mandatory Token createToken(@Mandatory String content, char ch) {
        return new Token(String.valueOf(ch), content.indexOf(ch));
    }
}