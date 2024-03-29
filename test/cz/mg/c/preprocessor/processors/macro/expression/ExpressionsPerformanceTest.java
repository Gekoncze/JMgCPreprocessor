package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.test.Performance;
import cz.mg.tokenizer.test.TokenFactory;

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
                f.bracket(")"),
                f.operator("+"),
                f.number("69"),
                f.operator("+"),
                f.number("0"),
                f.operator("*"),
                f.bracket("("),
                f.number("11"),
                f.operator("*"),
                f.number("11"),
                f.bracket(")")
            )));
        }
    }
}
