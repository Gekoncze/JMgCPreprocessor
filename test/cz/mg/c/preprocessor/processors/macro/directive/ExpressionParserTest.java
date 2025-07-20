package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;
import cz.mg.token.test.TokenAssertions;

public @Test class ExpressionParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ExpressionParserTest.class.getSimpleName() + " ... ");

        ExpressionParserTest test = new ExpressionParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service ExpressionParser parser = ExpressionParser.getInstance();
    private final @Service TokenAssertions assertions = TokenAssertions.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("if"),
                f.number("69")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            expression -> assertions.assertEquals(
                new List<>(f.number("69")),
                expression
            )
        );

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("if"),
                f.number("6"),
                f.symbol("+"),
                f.number("9")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            expression -> assertions.assertEquals(
                new List<>(f.number("6"), f.symbol("+"), f.number("9")),
                expression
            )
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("if"),
                f.whitespace(" "),
                f.number("6"),
                f.whitespace(" "),
                f.symbol("+"),
                f.whitespace(" "),
                f.number("9"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            expression -> assertions.assertEquals(
                new List<>(
                    f.whitespace(" "),
                    f.number("6"),
                    f.whitespace(" "),
                    f.symbol("+"),
                    f.whitespace(" "),
                    f.number("9"),
                    f.whitespace(" ")
                ),
                expression
            )
        );

        mutator.mutate(
            new List<>(
                f.symbol("#"),
                f.word("if"),
                f.number("69"),
                f.symbol("*"),
                f.symbol("("),
                f.number("1"),
                f.symbol("&&"),
                f.number("0"),
                f.symbol(")")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            expression -> assertions.assertEquals(
                new List<>(
                    f.number("69"),
                    f.symbol("*"),
                    f.symbol("("),
                    f.number("1"),
                    f.symbol("&&"),
                    f.number("0"),
                    f.symbol(")")
                ),
                expression
            )
        );

        Assert
            .assertThatCode(() -> parser.parse(new List<>()))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.symbol("#"))))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.symbol("#"), f.word("if"))))
            .throwsException(PreprocessorException.class);
    }
}
