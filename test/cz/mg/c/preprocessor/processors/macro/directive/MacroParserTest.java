package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.test.MacroValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;

public @Test class MacroParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroParserTest.class.getSimpleName() + " ... ");

        MacroParserTest test = new MacroParserTest();
        test.testNoParametersAndNoImplementation();
        test.testParametersAndNoImplementation();
        test.testNoParametersAndImplementation();
        test.testParametersAndImplementation();
        test.testVarargParameters();
        test.testLeadingBracketsExpression();
        test.testErrors();

        System.out.println("OK");
    }

    private final @Service MacroParser parser = MacroParser.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service MacroValidator validator = MacroValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();

    private void testNoParametersAndNoImplementation() {
        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.word("define"),
                f.whitespace(" "),
                f.word("LOREM_IPSUM")
            ),
            new List<>(0, 1, 3),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.word("LOREM_IPSUM"), null, new List<>()),
                macro
            )
        );
    }

    private void testParametersAndNoImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.whitespace(" "),
                f.word("LOREM_IPSUM"),
                f.bracket("("),
                f.bracket(")")
            ),
            new List<>(0, 1, 3, 5),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.word("LOREM_IPSUM"), new List<>(), new List<>()),
                macro
            )
        );
    }

    private void testNoParametersAndImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.whitespace(" "),
                f.word("TEST"),
                f.word("foo"),
                f.word("bar")
            ),
            new List<>(0, 1, 3),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.word("TEST"), null, new List<>(f.word("foo"), f.word("bar"))),
                macro
            )
        );
    }

    private void testParametersAndImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.whitespace(" "),
                f.word("PLUS"),
                f.bracket("("),
                f.whitespace(" "),
                f.word("x"),
                f.whitespace(" "),
                f.separator(","),
                f.whitespace(" "),
                f.word("y"),
                f.whitespace(" "),
                f.bracket(")"),
                f.word("x"),
                f.operator("+"),
                f.word("y")
            ),
            new List<>(0, 1, 3, 5, 6, 11, 12),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(
                    f.word("PLUS"),
                    new List<>(f.word("x"), f.word("y")),
                    new List<>(f.word("x"), f.operator("+"), f.word("y"))
                ),
                macro
            )
        );
    }

    private void testVarargParameters() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.whitespace(" "),
                f.word("PLUS"),
                f.bracket("("),
                f.operator("..."),
                f.bracket(")")
            ),
            new List<>(0, 1, 2, 3, 5, 6),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(
                    f.word("PLUS"),
                    new List<>(f.word(""), f.operator("...")),
                    new List<>()
                ),
                macro
            )
        );

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.word("x"),
                f.operator("..."),
                f.bracket(")")
            ),
            new List<>(0, 1, 2, 4, 5, 6),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(
                    f.word("PLUS"),
                    new List<>(f.word("x"), f.operator("...")),
                    new List<>()
                ),
                macro
            )
        );

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.word("x"),
                f.separator(","),
                f.operator("..."),
                f.bracket(")")
            ),
            new List<>(0, 1, 2, 4, 5, 6, 7),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(
                    f.word("PLUS"),
                    new List<>(f.word("x"), f.word(""), f.operator("...")),
                    new List<>()
                ),
                macro
            )
        );

        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.word("x"),
                f.separator(","),
                f.word("y"),
                f.operator("..."),
                f.bracket(")")
            ),
            new List<>(0, 1, 2, 4, 5, 6, 7, 8),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(
                    f.word("PLUS"),
                    new List<>(f.word("x"), f.word("y"), f.operator("...")),
                    new List<>()
                ),
                macro
            )
        );
    }

    private void testLeadingBracketsExpression() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.word("define"),
                f.whitespace(" "),
                f.word("LOREM_IPSUM"),
                f.whitespace(" "),
                f.bracket("("),
                f.whitespace(" "),
                f.bracket(")"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 3),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.word("LOREM_IPSUM"), null, new List<>(
                    f.whitespace(" "),
                    f.bracket("("),
                    f.whitespace(" "),
                    f.bracket(")"),
                    f.whitespace(" ")
                )),
                macro
            )
        );
    }

    private void testErrors() {
        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("(")
            ));
        }).throwsException(PreprocessorException.class);

        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.separator(","),
                f.bracket(")")
            ));
        }).throwsException(PreprocessorException.class);

        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.word("x"),
                f.word("y"),
                f.bracket(")")
            ));
        }).throwsException(PreprocessorException.class);

        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.operator("..."),
                f.word("x"),
                f.bracket(")")
            ));
        }).throwsException(PreprocessorException.class);

        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.word("define"),
                f.word("PLUS"),
                f.bracket("("),
                f.operator("..."),
                f.operator("..."),
                f.bracket(")")
            ));
        }).throwsException(PreprocessorException.class);
    }
}
