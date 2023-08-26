package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Test class TokenConcatenationServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + TokenConcatenationServiceTest.class.getSimpleName() + " ... ");

        TokenConcatenationServiceTest test = new TokenConcatenationServiceTest();
        test.testConcatenate();

        System.out.println("OK");
    }

    private final @Service TokenConcatenationService service = TokenConcatenationService.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testConcatenate() {
        testConcatenate(
            new List<>(),
            new List<>()
        );

        testConcatenate(
            new List<>(f.special("##")),
            new List<>(f.special("##"))
        );

        testConcatenate(
            new List<>(f.name("a"), f.special("##")),
            new List<>(f.name("a"), f.special("##"))
        );

        testConcatenate(
            new List<>(f.special("##"), f.name("a")),
            new List<>(f.special("##"), f.name("a"))
        );

        testConcatenate(
            new List<>(new NameToken("foo", 3), new SpecialToken("##", 6), new NameToken("bar", 8)),
            new List<>(new NameToken("foobar", 3))
        );

        testConcatenate(
            new List<>(f.name("f"), f.name("foo"), f.special("##"), f.name("bar"), f.name("b")),
            new List<>(f.name("f"), f.name("foobar"), f.name("b"))
        );
    }

    private void testConcatenate(@Mandatory List<Token> input, @Mandatory List<Token> output) {
        service.concatenate(input);
        validator.assertEquals(output, input);
    }
}
