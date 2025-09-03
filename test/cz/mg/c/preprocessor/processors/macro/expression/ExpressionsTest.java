package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.test.Assertions;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.token.test.TokenFactory;

public @Test class ExpressionsTest {
    public static void main(String[] args) {
        System.out.print("Running " + ExpressionsTest.class.getSimpleName() + " ... ");

        ExpressionsTest test = new ExpressionsTest();
        test.testSingleEvaluate();
        test.testUnaryNotEvaluate();
        test.testBinaryAndEvaluate();
        test.testBinaryOrEvaluate();
        test.testComplexEvaluate();
        test.testWhitespaceEvaluate();
        test.testIllegalExpression();

        System.out.println("OK");
    }

    private final @Service Expressions evaluator = Expressions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testSingleEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("-1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("7")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.word("one")
        )));
    }

    private void testUnaryNotEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.symbol("!"),
            f.number("7")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.symbol("!"),
            f.number("0")
        )));
    }

    private void testBinaryAndEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0"),
            f.symbol("&&"),
            f.number("0")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("&&"),
            f.number("0")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0"),
            f.symbol("&&"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("&&"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("3"),
            f.symbol("&&"),
            f.number("7")
        )));
    }

    private void testBinaryOrEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0"),
            f.symbol("||"),
            f.number("0")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("||"),
            f.number("0")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("0"),
            f.symbol("||"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("||"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("3"),
            f.symbol("||"),
            f.number("7")
        )));
    }

    private void testComplexEvaluate() {
        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("&&"),
            f.symbol("("),
            f.number("0"),
            f.symbol("||"),
            f.number("1"),
            f.symbol(")")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("&&"),
            f.symbol("("),
            f.number("0"),
            f.symbol("||"),
            f.number("0"),
            f.symbol(")")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("&&"),
            f.symbol("("),
            f.symbol("("),
            f.number("1"),
            f.symbol("&&"),
            f.number("1"),
            f.symbol("&&"),
            f.number("0"),
            f.symbol(")"),
            f.symbol("||"),
            f.number("0"),
            f.symbol(")")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.symbol("&&"),
            f.symbol("("),
            f.symbol("("),
            f.number("1"),
            f.symbol("&&"),
            f.number("1"),
            f.symbol("&&"),
            f.number("1"),
            f.symbol(")"),
            f.symbol("||"),
            f.number("0"),
            f.symbol(")")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("4"),
            f.symbol("=="),
            f.number("7"),
            f.symbol("-"),
            f.symbol("("),
            f.symbol("-"),
            f.number("2"),
            f.symbol("+"),
            f.number("5"),
            f.symbol(")")
        )));
    }

    private void testWhitespaceEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.whitespace(" "),
            f.number("1"),
            f.whitespace(" "),
            f.symbol("&&"),
            f.whitespace(" "),
            f.symbol("("),
            f.whitespace(" "),
            f.symbol("("),
            f.whitespace(" "),
            f.number("1"),
            f.whitespace(" "),
            f.symbol("&&"),
            f.whitespace(" "),
            f.number("1"),
            f.whitespace(" "),
            f.symbol("&&"),
            f.whitespace(" "),
            f.number("0"),
            f.whitespace(" "),
            f.symbol(")"),
            f.whitespace(" "),
            f.symbol("||"),
            f.whitespace(" "),
            f.number("0"),
            f.whitespace(" "),
            f.symbol(")"),
            f.whitespace(" ")
        )));
    }

    private void testIllegalExpression() {
        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("("),
            f.symbol(")")
        ))).throwsException(TraceableException.class);

        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("+")
        ))).throwsException(TraceableException.class);

        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("("),
            f.symbol("+"),
            f.symbol(")")
        ))).throwsException(TraceableException.class);

        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("1")
        ))).throwsException(TraceableException.class);

        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("("),
            f.symbol("1"),
            f.symbol(")")
        ))).throwsException(TraceableException.class);

        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("("),
            f.symbol("1")
        ))).throwsException(TraceableException.class);

        Assertions.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.symbol("1"),
            f.symbol(")")
        ))).throwsException(TraceableException.class);
    }
}
