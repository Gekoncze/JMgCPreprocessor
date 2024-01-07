package cz.mg.c.preprocessor.processors.macro.expression;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.Macros;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.test.TokenFactory;

public @Test class MacroExpressionsTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroExpressionsTest.class.getSimpleName() + " ... ");

        MacroExpressionsTest test = new MacroExpressionsTest();
        test.testEvaluateNotDefined();
        test.testEvaluateDefined();
        test.testEvaluateCombination1();
        test.testEvaluateCombination2();

        System.out.println("OK");
    }

    private final @Service MacroExpressions expressions = MacroExpressions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testEvaluateNotDefined() {
        MacroManager macros = new MacroManager(new Macros());
        List<Token> tokens = new List<>(f.word("defined"), f.bracket("("), f.word("FOOBAR"), f.bracket(")"));
        Assert.assertEquals(false, expressions.evaluate(tokens, macros));
        Assert.assertEquals(false, macros.defined("defined"));
    }

    private void testEvaluateDefined() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOOBAR"), null, new List<>()));
        List<Token> tokens = new List<>(f.word("defined"), f.bracket("("), f.word("FOOBAR"), f.bracket(")"));
        Assert.assertEquals(true, expressions.evaluate(tokens, macros));
        Assert.assertEquals(false, macros.defined("defined"));
    }

    private void testEvaluateCombination1() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOOBAR"), null, new List<>()));
        List<Token> tokens = new List<>(
            f.word("defined"),
            f.bracket("("),
            f.word("FOOBAR"),
            f.bracket(")"),
            f.operator("&&"),
            f.word("defined"),
            f.bracket("("),
            f.word("BARFOO"),
            f.bracket(")")
        );
        Assert.assertEquals(false, expressions.evaluate(tokens, macros));
        Assert.assertEquals(false, macros.defined("defined"));
    }

    private void testEvaluateCombination2() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOOBAR"), null, new List<>()));
        List<Token> tokens = new List<>(
            f.word("defined"),
            f.bracket("("),
            f.word("FOOBAR"),
            f.bracket(")"),
            f.operator("||"),
            f.word("defined"),
            f.bracket("("),
            f.word("BARFOO"),
            f.bracket(")")
        );
        Assert.assertEquals(true, expressions.evaluate(tokens, macros));
        Assert.assertEquals(false, macros.defined("defined"));
    }
}
