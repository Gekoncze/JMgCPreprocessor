package cz.mg.c.preprocessor.expression;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.TokenFactory;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.test.Performance;

public @Test class ExpressionProcessorPerformanceTest {
    private static final int ITERATIONS = 10;

    public static void main(String[] args) {
        System.out.print("Running " + ExpressionProcessorPerformanceTest.class.getSimpleName() + " ... ");

        ExpressionProcessorPerformanceTest test = new ExpressionProcessorPerformanceTest();
        test.testPerformance();
    }

    private final ExpressionProcessor expressionProcessor = ExpressionProcessor.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testPerformance() {
        System.out.println(Performance.measure(this::testCase, ITERATIONS) + " ms");
    }

    private void testCase() {
        Assert.assertEquals(true, expressionProcessor.evaluate(new List<>(
            f.number("1"),
            f.operator("=="),
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
