package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenValidator;

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
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IncludeDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("include"),
                f.doubleQuote("stdio.h")
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(false, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.word("include"),
                f.whitespace(" "),
                f.doubleQuote("stdio.h"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(false, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("include"),
                f.operator("<"),
                f.word("stdio"),
                f.operator("."),
                f.word("h"),
                f.operator(">")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(true, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.word("include"),
                f.whitespace(" "),
                f.operator("<"),
                f.whitespace(" "),
                f.word("stdio"),
                f.operator("."),
                f.word("h"),
                f.whitespace(" "),
                f.operator(">"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.word("include"), directive.getKeyword());
                Assert.assertEquals(true, directive.isLibrary());
                Assert.assertEquals("stdio.h", directive.getPath().toString());
            }
        );
    }
}
