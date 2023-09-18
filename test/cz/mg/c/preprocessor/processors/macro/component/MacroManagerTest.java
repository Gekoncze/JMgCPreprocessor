package cz.mg.c.preprocessor.processors.macro.component;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;

import java.util.NoSuchElementException;

public @Test class MacroManagerTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroManagerTest.class.getSimpleName() + " ... ");

        MacroManagerTest test = new MacroManagerTest();
        test.testDefine();
        test.testCalled();
        test.testTemporary();

        System.out.println("OK");
    }

    private void testDefine() {
        Macros macros = new Macros();
        MacroManager manager = new MacroManager(macros);

        Assert.assertEquals(false, manager.defined("MACRO"));
        Assert.assertEquals(false, manager.defined("foo"));
        Assert.assertThatCode(() -> manager.get("MACRO")).throwsException(NoSuchElementException.class);
        Assert.assertSame(null, manager.getOptional("MACRO"));
        Assert.assertSame(null, manager.getOptional("foo"));
        Assert.assertEquals(true, macros.getDefinitions().isEmpty());

        Macro first = new Macro(new Token("MACRO", 0), new List<>(), new List<>());
        Macro second = new Macro(new Token("MACRO", 0), new List<>(), new List<>(new NameToken("foo", 0)));

        manager.define(first);

        Assert.assertEquals(true, manager.defined("MACRO"));
        Assert.assertEquals(false, manager.defined("foo"));
        Assert.assertSame(first, manager.get("MACRO"));
        Assert.assertSame(first, manager.getOptional("MACRO"));
        Assert.assertSame(null, manager.getOptional("foo"));
        Assert.assertSame(first, macros.getDefinitions().getFirst());

        manager.define(second);

        Assert.assertEquals(true, manager.defined("MACRO"));
        Assert.assertEquals(false, manager.defined("foo"));
        Assert.assertSame(second, manager.get("MACRO"));
        Assert.assertSame(second, manager.getOptional("MACRO"));
        Assert.assertSame(null, manager.getOptional("foo"));
        Assert.assertSame(second, macros.getDefinitions().getFirst());

        manager.undefine("MACRO");

        Assert.assertEquals(false, manager.defined("MACRO"));
        Assert.assertEquals(false, manager.defined("foo"));
        Assert.assertThatCode(() -> manager.get("MACRO")).throwsException(NoSuchElementException.class);
        Assert.assertSame(null, manager.getOptional("MACRO"));
        Assert.assertSame(null, manager.getOptional("foo"));
        Assert.assertEquals(true, macros.getDefinitions().isEmpty());
    }

    private void testCalled() {
        Macros macros = new Macros();
        MacroManager manager = new MacroManager(macros);
        MacroCall call = new MacroCall();

        manager.called(call);

        Assert.assertSame(call, macros.getCalls().getFirst());
    }

    private void testTemporary() {
        Macros macros = new Macros();
        MacroManager manager = new MacroManager(macros);
        Macro macro = new Macro(new Token("MACRO", 0), new List<>(), new List<>());

        Integer result = manager.temporary(() -> {
            Assert.assertSame(macro, macros.getDefinitions().getFirst());
            return 1;
        }, macro);

        Assert.assertEquals(1, result);
        Assert.assertEquals(true, macros.getDefinitions().isEmpty());
    }
}
