package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.macro.entities.Macro;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
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
    private final TokenValidator validator = TokenValidator.getInstance();

    private void testNoParametersAndNoImplementation() {
        Macro macro = parser.parse(new List<>(
            f.special("#"),
            f.name("define"),
            f.name("LOREM_IPSUM")
        ));

        Assert.assertEquals("LOREM_IPSUM", macro.getName().getText());
        Assert.assertNull(macro.getParameters());
        validator.assertNameEquals(new List<>(), macro.getTokens());
    }

    private void testParametersAndNoImplementation() {
        Macro macro = parser.parse(new List<>(
            f.special("#"),
            f.name("define"),
            f.name("LOREM_IPSUM"),
            f.bracket("("),
            f.bracket(")")
        ));

        Assert.assertEquals("LOREM_IPSUM", macro.getName().getText());
        Assert.assertNotNull(macro.getParameters());
        validator.assertNameEquals(new List<>(), macro.getTokens());
    }

    private void testNoParametersAndImplementation() {
        Macro macro = parser.parse(new List<>(
            f.special("#"),
            f.name("define"),
            f.name("TEST"),
            f.name("foo"),
            f.name("bar")
        ));

        Assert.assertEquals("TEST", macro.getName().getText());
        Assert.assertNull(macro.getParameters());
        validator.assertNameEquals(new List<>("foo", "bar"), macro.getTokens());
    }

    private void testParametersAndImplementation() {
        Macro macro = parser.parse(new List<>(
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
        ));

        Assert.assertEquals("PLUS", macro.getName().getText());
        Assert.assertNotNull(macro.getParameters());
        validator.assertNameEquals(new List<>("x", "y"), macro.getParameters());
        validator.assertNameEquals(new List<>("x", "+", "y"), macro.getTokens());
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
