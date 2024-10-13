package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.directives.DefineDirective;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenAssertions;

public @Test class DirectiveParsersTest {
    public static void main(String[] args) {
        System.out.print("Running " + DirectiveParsersTest.class.getSimpleName() + " ... ");

        DirectiveParsersTest test = new DirectiveParsersTest();
        test.testParseDirectiveLine();
        test.testParseNonDirectiveLine();
        test.testParseUnsupportedDirectiveLine();

        System.out.println("OK");
    }

    private final @Service DirectiveParsers parsers = DirectiveParsers.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenAssertions assertions = TokenAssertions.getInstance();

    private void testParseDirectiveLine() {
        DefineDirective directive = (DefineDirective) parsers.parse(
            new List<>(f.symbol("#"), f.word("define"), f.word("TEST"))
        );

        Assert.assertNotNull(directive);
        assertions.assertEquals(f.word("define"), directive.getKeyword());
        Assert.assertNotNull(directive.getMacro());
    }

    private void testParseNonDirectiveLine() {
        Assert.assertNull(parsers.parse(new List<>(f.word("define"), f.word("TEST"))));
    }

    private void testParseUnsupportedDirectiveLine() {
        Assert.assertThatCode(() -> {
            parsers.parse(new List<>(f.symbol("#"), f.word("unknown"), f.word("TEST")));
        }).throwsException(TraceableException.class);
    }
}
