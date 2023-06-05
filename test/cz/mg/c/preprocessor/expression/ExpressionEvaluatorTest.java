package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;

public @Test class ExpressionEvaluatorTest {
    public static void main(String[] args) {
        System.out.print("Running " + ExpressionEvaluatorTest.class.getSimpleName() + " ... ");

        ExpressionEvaluatorTest test = new ExpressionEvaluatorTest();
        test.testSingleEvaluate();
        test.testBinaryAndEvaluate();
        test.testBinaryOrEvaluate();
        test.testComplexEvaluate();

        System.out.println("OK");
    }

    private final ExpressionEvaluator evaluator = ExpressionEvaluator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testSingleEvaluate() {
        Assert.assertEquals(true, evaluator.evaluate(new List<>(
            f.number("1")
        )));

        Assert.assertEquals(false, evaluator.evaluate(new List<>(
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
    }
}
