package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;

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

    private final Expressions evaluator = Expressions.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

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
    }

    private void testUnaryNotEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.operator("!"),
            f.number("7")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.operator("!"),
            f.number("0")
        )));
    }

    private void testBinaryAndEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0"),
            f.operator("&&"),
            f.number("0")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.number("0")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0"),
            f.operator("&&"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("3"),
            f.operator("&&"),
            f.number("7")
        )));
    }

    private void testBinaryOrEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("0"),
            f.operator("||"),
            f.number("0")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("||"),
            f.number("0")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("0"),
            f.operator("||"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("||"),
            f.number("1")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("3"),
            f.operator("||"),
            f.number("7")
        )));
    }

    private void testComplexEvaluate() {
        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.bracket("("),
            f.number("0"),
            f.operator("||"),
            f.number("1"),
            f.bracket(")")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.bracket("("),
            f.number("0"),
            f.operator("||"),
            f.number("0"),
            f.bracket(")")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.bracket("("),
            f.bracket("("),
            f.number("1"),
            f.operator("&&"),
            f.number("1"),
            f.operator("&&"),
            f.number("0"),
            f.bracket(")"),
            f.operator("||"),
            f.number("0"),
            f.bracket(")")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.bracket("("),
            f.bracket("("),
            f.number("1"),
            f.operator("&&"),
            f.number("1"),
            f.operator("&&"),
            f.number("1"),
            f.bracket(")"),
            f.operator("||"),
            f.number("0"),
            f.bracket(")")
        )));

        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("4"),
            f.operator("=="),
            f.number("7"),
            f.operator("-"),
            f.bracket("("),
            f.operator("-"),
            f.number("2"),
            f.operator("+"),
            f.number("5"),
            f.bracket(")")
        )));
    }

    private void testWhitespaceEvaluate() {
        Assert.assertEquals(false, evaluator.evaluate(new List<>(
            f.whitespace(" "),
            f.number("1"),
            f.whitespace(" "),
            f.operator("&&"),
            f.whitespace(" "),
            f.bracket("("),
            f.whitespace(" "),
            f.bracket("("),
            f.whitespace(" "),
            f.number("1"),
            f.whitespace(" "),
            f.operator("&&"),
            f.whitespace(" "),
            f.number("1"),
            f.whitespace(" "),
            f.operator("&&"),
            f.whitespace(" "),
            f.number("0"),
            f.whitespace(" "),
            f.bracket(")"),
            f.whitespace(" "),
            f.operator("||"),
            f.whitespace(" "),
            f.number("0"),
            f.whitespace(" "),
            f.bracket(")"),
            f.whitespace(" ")
        )));
    }

    private void testIllegalExpression() {
        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.operator("()")
        ))).throwsException(CodeException.class);

        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.operator("+")
        ))).throwsException(CodeException.class);

        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.bracket("("),
            f.operator("+"),
            f.bracket(")")
        ))).throwsException(CodeException.class);

        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.operator("1")
        ))).doesNotThrowAnyException();

        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.bracket("("),
            f.operator("1"),
            f.bracket(")")
        ))).doesNotThrowAnyException();

        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.bracket("("),
            f.operator("1")
        ))).throwsException(CodeException.class);

        Assert.assertThatCode(() -> evaluator.evaluate(new List<>(
            f.operator("1"),
            f.bracket(")")
        ))).throwsException(CodeException.class);
    }
}
