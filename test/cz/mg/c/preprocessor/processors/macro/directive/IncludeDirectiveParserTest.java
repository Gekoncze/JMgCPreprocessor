package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;
import cz.mg.token.test.TokenAssertions;

public @Test class IncludeDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + IncludeDirectiveParserTest.class.getSimpleName() + " ... ");

        IncludeDirectiveParserTest test = new IncludeDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service IncludeDirectiveParser parser = IncludeDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IncludeDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("include"),
                f.doubleQuote("stdio.h")
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(false, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("include"),
                f.whitespace(" "),
                f.doubleQuote("stdio.h"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(false, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("include"),
                f.symbol("<"),
                f.word("stdio"),
                f.symbol("."),
                f.word("h"),
                f.symbol(">")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(true, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("include"),
                f.whitespace(" "),
                f.symbol("<"),
                f.whitespace(" "),
                f.word("stdio"),
                f.symbol("."),
                f.word("h"),
                f.whitespace(" "),
                f.symbol(">"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(true, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );
    }
}
