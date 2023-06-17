package cz.mg.c.preprocessor.processors.macro.services;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.test.MacroValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
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

    private void testNoParametersAndNoImplementation() {
        validator.assertEquals(
            new Macro(f.name("LOREM_IPSUM"), null, new List<>()),
            parser.parse(new List<>(
                f.special("#"),
                f.name("define"),
                f.name("LOREM_IPSUM")
            ))
        );
    }

    private void testParametersAndNoImplementation() {
        validator.assertEquals(
            new Macro(f.name("LOREM_IPSUM"), new List<>(), new List<>()),
            parser.parse(new List<>(
                f.special("#"),
                f.name("define"),
                f.name("LOREM_IPSUM"),
                f.bracket("("),
                f.bracket(")")
            ))
        );
    }

    private void testNoParametersAndImplementation() {
        validator.assertEquals(
            new Macro(f.name("TEST"), null, new List<>(f.name("foo"), f.name("bar"))),
            parser.parse(new List<>(
                f.special("#"),
                f.name("define"),
                f.name("TEST"),
                f.name("foo"),
                f.name("bar")
            ))
        );
    }

    private void testParametersAndImplementation() {
        validator.assertEquals(
            new Macro(
                f.name("PLUS"),
                new List<>(f.name("x"), f.name("y")),
                new List<>(f.name("x"), f.operator("+"), f.name("y"))
            ),
            parser.parse(new List<>(
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
            ))
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
