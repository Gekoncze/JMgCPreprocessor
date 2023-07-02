package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.test.MacroValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;

public @Test class MacroParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroParserTest.class.getSimpleName() + " ... ");

        MacroParserTest test = new MacroParserTest();
        test.testNoParametersAndNoImplementation();
        test.testParametersAndNoImplementation();
        test.testNoParametersAndImplementation();
        test.testParametersAndImplementation();
        test.testErrors();

        System.out.println("OK");
    }

    private final MacroParser parser = MacroParser.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();
    private final MacroValidator validator = MacroValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();

    private void testNoParametersAndNoImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("define"),
                f.name("LOREM_IPSUM")
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.name("LOREM_IPSUM"), null, new List<>()),
                macro
            )
        );
    }

    private void testParametersAndNoImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("define"),
                f.name("LOREM_IPSUM"),
                f.bracket("("),
                f.bracket(")")
            ),
            new List<>(0, 1, 2, 4),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.name("LOREM_IPSUM"), new List<>(), new List<>()),
                macro
            )
        );
    }

    private void testNoParametersAndImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("define"),
                f.name("TEST"),
                f.name("foo"),
                f.name("bar")
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(f.name("TEST"), null, new List<>(f.name("foo"), f.name("bar"))),
                macro
            )
        );
    }

    private void testParametersAndImplementation() {
        mutator.mutate(
            new List<>(
                f.special("#"),
                f.name("define"),
                f.name("PLUS"),
                f.bracket("("),
                f.name("x"),
                f.separator(","),
                f.name("y"),
                f.bracket(")"),
                f.name("x"),
                f.operator("+"),
                f.name("y")
            ),
            new List<>(0, 1, 2, 4, 5, 6, 7),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(
                    f.name("PLUS"),
                    new List<>(f.name("x"), f.name("y")),
                    new List<>(f.name("x"), f.operator("+"), f.name("y"))
                ),
                macro
            )
        );
    }

    private void testErrors() {
        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.name("define"),
                f.name("PLUS"),
                f.bracket("(")
            ));
        }).throwsException(PreprocessorException.class);

        Assert.assertThatCode(() -> {
            parser.parse(new List<>(
                f.special("#"),
                f.name("define"),
                f.name("PLUS"),
                f.bracket("("),
                f.separator(","),
                f.bracket(")")
            ));
        }).throwsException(PreprocessorException.class);
    }
}
