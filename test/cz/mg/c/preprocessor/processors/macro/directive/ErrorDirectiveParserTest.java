package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;

public @Test class ErrorDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ErrorDirectiveParserTest.class.getSimpleName() + " ... ");

        ErrorDirectiveParserTest test = new ErrorDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service ErrorDirectiveParser parser = ErrorDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(ErrorDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("error")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                TokenAssert.assertEquals(f.word("error"), directive.getKeyword());
                Assert.assertNull(directive.getMessage());
            }
        );

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("error"), f.whitespace(" ")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                TokenAssert.assertEquals(f.word("error"), directive.getKeyword());
                Assert.assertNull(directive.getMessage());
            }
        );

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("error"), f.word("oi"), f.word("oi"), f.word("oi")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                TokenAssert.assertEquals(f.word("error"), directive.getKeyword());
                Assert.assertEquals("oioioi", directive.getMessage());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("error"),
                f.whitespace(" "),
                f.word("oi"),
                f.whitespace(" "),
                f.word("oi"),
                f.whitespace(" "),
                f.word("oi"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> {
                TokenAssert.assertEquals(f.word("error"), directive.getKeyword());
                Assert.assertEquals("oi oi oi", directive.getMessage());
            }
        );
    }
}
