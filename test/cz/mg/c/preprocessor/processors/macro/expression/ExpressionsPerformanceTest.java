package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.test.Performance;
import cz.mg.token.test.TokenFactory;

public @Test class ExpressionsPerformanceTest {
    private static final int ITERATIONS = 10;
    private static final int LOOPS = 100000;

    public static void main(String[] args) {
        System.out.print("Running " + ExpressionsPerformanceTest.class.getSimpleName() + " ... ");

        ExpressionsPerformanceTest test = new ExpressionsPerformanceTest();
        test.testPerformance();
    }

    private final @Service Expressions evaluator = Expressions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testPerformance() {
        System.out.println(Performance.measure(this::testCase, ITERATIONS) + " ms");
    }

    private void testCase() {
        for (int i = 0; i < LOOPS; i++) {
            Assert.assertEquals(true, evaluator.evaluate(new List<>(
                f.number("1"),
                f.symbol("=="),
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
                f.symbol(")"),
                f.symbol("+"),
                f.number("69"),
                f.symbol("+"),
                f.number("0"),
                f.symbol("*"),
                f.symbol("("),
                f.number("11"),
                f.symbol("*"),
                f.number("11"),
                f.symbol(")")
            )));
        }
    }
}
