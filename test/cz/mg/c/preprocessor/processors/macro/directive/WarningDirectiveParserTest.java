package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenAssertions;

public @Test class WarningDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + WarningDirectiveParserTest.class.getSimpleName() + " ... ");

        WarningDirectiveParserTest test = new WarningDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service WarningDirectiveParser parser = WarningDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(WarningDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("warning")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("warning"), directive.getKeyword());
                Assert.assertNull(directive.getMessage());
            }
        );

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("warning"),
                f.whitespace(" ")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("warning"), directive.getKeyword());
                Assert.assertNull(directive.getMessage());
            }
        );

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("warning"),
                f.word("doko"),
                f.word("doko"),
                f.word("doko")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("warning"), directive.getKeyword());
                Assert.assertEquals("dokodokodoko", directive.getMessage());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("warning"),
                f.whitespace(" "),
                f.word("doko"),
                f.whitespace(" "),
                f.word("doko"),
                f.whitespace(" "),
                f.word("doko"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("warning"), directive.getKeyword());
                Assert.assertEquals("doko doko doko", directive.getMessage());
            }
        );
    }
}
