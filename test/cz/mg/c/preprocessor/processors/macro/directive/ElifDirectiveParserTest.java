package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;
import cz.mg.token.test.TokenAssertions;

public @Test class ElifDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ElifDirectiveParserTest.class.getSimpleName() + " ... ");

        ElifDirectiveParserTest test = new ElifDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service ElifDirectiveParser parser = ElifDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(ElifDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("elif"), f.number("1"), f.symbol("<"), f.number("2")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("elif"), directive.getKeyword());
                tokenAssertions.assertEquals(
                    new List<>(f.number("1"), f.symbol("<"), f.number("2")),
                    directive.getExpression()
                );
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("elif"),
                f.whitespace(" "),
                f.number("1"),
                f.whitespace(" "),
                f.symbol("<"),
                f.whitespace(" "),
                f.number("2"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("elif"), directive.getKeyword());
                tokenAssertions.assertEquals(
                    new List<>(
                        f.whitespace(" "),
                        f.number("1"),
                        f.whitespace(" "),
                        f.symbol("<"),
                        f.whitespace(" "),
                        f.number("2"),
                        f.whitespace(" ")
                    ),
                    directive.getExpression()
                );
            }
        );
    }
}
