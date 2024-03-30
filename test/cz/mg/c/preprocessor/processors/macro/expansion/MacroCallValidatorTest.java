package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.test.TokenFactory;

public @Test class MacroCallValidatorTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroCallValidatorTest.class.getSimpleName() + " ... ");

        MacroCallValidatorTest test = new MacroCallValidatorTest();
        test.testMacroCallWithArguments();
        test.testMacroCallWithoutArguments();
        test.testMacroCallArgumentCount();
        test.testMacroCallEmptyArguments();

        System.out.println("OK");
    }

    private final @Service MacroCallValidator validator = MacroCallValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testMacroCallWithArguments() {
        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), new List<>(), new List<>());
            MacroCall call = new MacroCall(macro, f.word("FOO"), new List<>());
            validator.validate(call);
        }).doesNotThrowAnyException();

        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), null, new List<>());
            MacroCall call = new MacroCall(macro, f.word("FOO"), new List<>());
            validator.validate(call);
        }).throwsException(TraceableException.class);
    }

    private void testMacroCallWithoutArguments() {
        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), new List<>(), new List<>());
            MacroCall call = new MacroCall(macro, f.word("FOO"), null);
            validator.validate(call);
        }).throwsException(TraceableException.class);

        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), null, new List<>());
            MacroCall call = new MacroCall(macro, f.word("FOO"), null);
            validator.validate(call);
        }).doesNotThrowAnyException();
    }

    private void testMacroCallArgumentCount() {
        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), new List<>(f.word("x"), f.word("y")), new List<>());
            MacroCall call = new MacroCall(
                macro,
                f.word("FOO"),
                new List<>(new List<>(f.symbol("*"), f.word("ptr")), new List<>(f.word("a")))
            );
            validator.validate(call);
        }).doesNotThrowAnyException();

        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), new List<>(f.word("x"), f.word("y")), new List<>());
            MacroCall call = new MacroCall(
                macro,
                f.word("FOO"),
                new List<List<Token>>(new List<>(f.symbol("*"), f.word("ptr")))
            );
            validator.validate(call);
        }).throwsException(TraceableException.class);
    }

    private void testMacroCallEmptyArguments() {
        Assert.assertThatCode(() -> {
            Macro macro = new Macro(f.word("FOO"), new List<>(f.word("x"), f.word("y")), new List<>());
            MacroCall call = new MacroCall(
                macro,
                f.word("FOO"),
                new List<>(new List<>(), new List<>())
            );
            validator.validate(call);
        }).throwsException(TraceableException.class);
    }
}
