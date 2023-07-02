package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.directives.DefineDirective;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;

public @Test class DirectiveParsersTest {
    public static void main(String[] args) {
        System.out.print("Running " + DirectiveParsersTest.class.getSimpleName() + " ... ");

        DirectiveParsersTest test = new DirectiveParsersTest();
        test.testParseDirectiveLine();
        test.testParseNonDirectiveLine();
        test.testParseUnsupportedDirectiveLine();

        System.out.println("OK");
    }

    private final DirectiveParsers parsers = DirectiveParsers.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();
    private final TokenValidator validator = TokenValidator.getInstance();

    private void testParseDirectiveLine() {
        DefineDirective directive = (DefineDirective) parsers.parse(
            new List<>(f.special("#"), f.name("define"), f.name("TEST"))
        );

        Assert.assertNotNull(directive);
        validator.assertEquals(f.name("define"), directive.getKeyword());
        Assert.assertNotNull(directive.getMacro());
    }

    private void testParseNonDirectiveLine() {
        Assert.assertNull(parsers.parse(new List<>(f.name("define"), f.name("TEST"))));
    }

    private void testParseUnsupportedDirectiveLine() {
        Assert.assertThatCode(() -> {
            parsers.parse(new List<>(f.special("#"), f.name("unknown"), f.name("TEST")));
        }).throwsException(CodeException.class);
    }
}
