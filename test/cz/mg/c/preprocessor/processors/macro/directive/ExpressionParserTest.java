package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class ExpressionParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ExpressionParserTest.class.getSimpleName() + " ... ");

        ExpressionParserTest test = new ExpressionParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service ExpressionParser parser = ExpressionParser.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("if"),
                f.number("69")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            expression -> validator.assertEquals(
                new List<>(f.number("69")),
                expression
            )
        );

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("if"),
                f.number("6"),
                f.operator("+"),
                f.number("9")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            expression -> validator.assertEquals(
                new List<>(f.number("6"), f.operator("+"), f.number("9")),
                expression
            )
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("if"),
                f.whitespace(" "),
                f.number("6"),
                f.whitespace(" "),
                f.operator("+"),
                f.whitespace(" "),
                f.number("9"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            expression -> validator.assertEquals(
                new List<>(
                    f.whitespace(" "),
                    f.number("6"),
                    f.whitespace(" "),
                    f.operator("+"),
                    f.whitespace(" "),
                    f.number("9"),
                    f.whitespace(" ")
                ),
                expression
            )
        );

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("if"),
                f.number("69"),
                f.operator("*"),
                f.bracket("("),
                f.number("1"),
                f.operator("&&"),
                f.number("0"),
                f.bracket(")")
            ),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            expression -> validator.assertEquals(
                new List<>(
                    f.number("69"),
                    f.operator("*"),
                    f.bracket("("),
                    f.number("1"),
                    f.operator("&&"),
                    f.number("0"),
                    f.bracket(")")
                ),
                expression
            )
        );

        Assert
            .assertThatCode(() -> parser.parse(new List<>()))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.special("#"))))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.special("#"), f.name("if"))))
            .throwsException(PreprocessorException.class);
    }
}
