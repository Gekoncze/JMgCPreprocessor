package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.TokenFactory;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;

public @Test class ExpressionProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + ExpressionProcessorTest.class.getSimpleName() + " ... ");

        ExpressionProcessorTest test = new ExpressionProcessorTest();
        test.testSingleEvaluate();
        test.testBinaryAndEvaluate();
        test.testBinaryOrEvaluate();
        test.testComplexEvaluate();

        System.out.println("OK");
    }

    private final ExpressionProcessor processor = ExpressionProcessor.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testSingleEvaluate() {
        Assert.assertEquals(true, processor.evaluate(new List<>(
            f.number("1")
        )));

        Assert.assertEquals(false, processor.evaluate(new List<>(
            f.number("0")
        )));
    }

    private void testBinaryAndEvaluate() {
        Assert.assertEquals(false, processor.evaluate(new List<>(
            f.number("0"),
            f.operator("&&"),
            f.number("0")
        )));

        Assert.assertEquals(false, processor.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.number("0")
        )));

        Assert.assertEquals(false, processor.evaluate(new List<>(
            f.number("0"),
            f.operator("&&"),
            f.number("1")
        )));

        Assert.assertEquals(true, processor.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.number("1")
        )));
    }

    private void testBinaryOrEvaluate() {
        Assert.assertEquals(false, processor.evaluate(new List<>(
            f.number("0"),
            f.operator("||"),
            f.number("0")
        )));

        Assert.assertEquals(true, processor.evaluate(new List<>(
            f.number("1"),
            f.operator("||"),
            f.number("0")
        )));

        Assert.assertEquals(true, processor.evaluate(new List<>(
            f.number("0"),
            f.operator("||"),
            f.number("1")
        )));

        Assert.assertEquals(true, processor.evaluate(new List<>(
            f.number("1"),
            f.operator("||"),
            f.number("1")
        )));
    }

    private void testComplexEvaluate() {
        Assert.assertEquals(true, processor.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.bracket("("),
            f.number("0"),
            f.operator("||"),
            f.number("1"),
            f.bracket(")")
        )));

        Assert.assertEquals(false, processor.evaluate(new List<>(
            f.number("1"),
            f.operator("&&"),
            f.bracket("("),
            f.number("0"),
            f.operator("||"),
            f.number("0"),
            f.bracket(")")
        )));

        Assert.assertEquals(false, processor.evaluate(new List<>(
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

        Assert.assertEquals(true, processor.evaluate(new List<>(
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
