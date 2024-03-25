package cz.mg.c.preprocessor.processors.macro.component;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.branch.MacroBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.branch.MacroCompletedBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.branch.MacroFalseBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.branch.MacroTrueBranchProcessor;
import cz.mg.c.preprocessor.processors.macro.components.MacroBranches;
import cz.mg.c.entities.directives.Directive;
import cz.mg.c.entities.directives.ElseDirective;
import cz.mg.c.entities.directives.EndifDirective;
import cz.mg.c.entities.directives.IfdefDirective;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.test.TokenFactory;

public @Test class MacroBranchesTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroBranchesTest.class.getSimpleName() + " ... ");

        MacroBranchesTest test = new MacroBranchesTest();
        test.test();

        System.out.println("OK");
    }

    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service MacroBranchProcessor trueBranch = MacroTrueBranchProcessor.getInstance();
    private final @Service MacroBranchProcessor falseBranch = MacroFalseBranchProcessor.getInstance();
    private final @Service MacroBranchProcessor completedBranch = MacroCompletedBranchProcessor.getInstance();

    /**
     * ...        // true branch
     * #ifdef FOO // true -> nest
     * ...        // true branch
     * #ifdef BAR // false -> skip
     * ...        // false branch
     * #else      // true -> nest
     * ...        // true branch
     * #endif     // BAR -> unnest + end
     * ...        // true branch
     * #else      // false -> unnest
     * ...        // completed branch
     * #endif     // FOO -> end
     * ...        // true branch
     */
    private void test() {
        MacroBranches branches = new MacroBranches();

        Directive fooBegin = new IfdefDirective(f.word("ifdef"), f.word("FOO"));
        Directive fooElse = new ElseDirective(f.word("else"));
        Directive barElse = new ElseDirective(f.word("else"));
        Directive barEnd = new EndifDirective(f.word("endif"));

        Assert.assertEquals(trueBranch, branches.getBranch());

        Assert.assertThatCode(() -> {
            branches.validateIsRoot();
        }).doesNotThrowAnyException();

        Assert.assertThatCode(() -> {
            branches.validateNotRoot(fooBegin);
        }).throwsException(TraceableException.class);

        Assert.assertThatCode(() -> {
            branches.unnest(fooBegin);
        }).throwsException(TraceableException.class);

        // #ifdef FOO
        branches.nest(fooBegin, true);
        Assert.assertEquals(trueBranch, branches.getBranch());

        Assert.assertThatCode(() -> {
            branches.validateIsRoot();
        }).throwsException(TraceableException.class);

        Assert.assertThatCode(() -> {
            branches.validateNotRoot(fooBegin);
        }).doesNotThrowAnyException();

        // #ifdef BAR
        branches.skip();
        Assert.assertEquals(falseBranch, branches.getBranch());

        // #else BAR
        branches.nest(barElse, true);
        Assert.assertEquals(trueBranch, branches.getBranch());

        // #endif BAR
        branches.unnest(barEnd);
        Assert.assertEquals(completedBranch, branches.getBranch());
        branches.end();
        Assert.assertEquals(trueBranch, branches.getBranch());

        // #else FOO
        branches.unnest(fooElse);
        Assert.assertEquals(completedBranch, branches.getBranch());

        // #endif FOO
        branches.end();
        Assert.assertEquals(trueBranch, branches.getBranch());

        Assert.assertThatCode(() -> {
            branches.validateIsRoot();
        }).doesNotThrowAnyException();
    }
}
