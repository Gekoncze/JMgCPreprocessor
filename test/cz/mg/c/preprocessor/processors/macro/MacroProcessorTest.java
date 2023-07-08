package cz.mg.c.preprocessor.processors.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.exceptions.ErrorException;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;

public @Test class MacroProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroProcessorTest.class.getSimpleName() + " ... ");

        MacroProcessorTest test = new MacroProcessorTest();
        test.testProcessing();
        test.testProcessingError();

        System.out.println("OK");
    }

    private final @Service MacroProcessor macroProcessor = MacroProcessor.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testProcessing() {
        Macros macros = new Macros();
        macros.define(new Macro(f.name("FOO"), null, new List<>()));
        macros.define(new Macro(f.name("DELETE_ME_NOW"), null, new List<>()));

        Assert.assertEquals(true, macros.defined("FOO"));
        Assert.assertEquals(true, macros.defined("DELETE_ME_NOW"));

        List<List<Token>> lines = new List<>(
            new List<>(f.number("1")),
            new List<>(f.special("#"), f.name("ifdef"), f.name("FOO")),
            new List<>(f.number("2")),
            new List<>(f.special("#"), f.name("ifdef"), f.name("BAR")),
            new List<>(f.number("3")),
            new List<>(f.special("#"), f.name("else")),
            new List<>(f.number("4")),
            new List<>(f.special("#"), f.name("endif")),
            new List<>(f.number("5")),
            new List<>(f.special("#"), f.name("else")),
            new List<>(f.number("6")),
            new List<>(f.special("#"), f.name("endif")),
            new List<>(f.number("7")),
            new List<>(f.special("#"), f.name("undef"), f.name("DELETE_ME_NOW"))
        );
        List<Token> actualTokens = macroProcessor.process(lines, macros);
        List<Token> expectedTokens = new List<>(
            f.number("1"),
            f.number("2"),
            f.number("4"),
            f.number("5"),
            f.number("7")
        );
        validator.assertEquals(expectedTokens, actualTokens);

        Assert.assertEquals(true, macros.defined("FOO"));
        Assert.assertEquals(false, macros.defined("DELETE_ME_NOW"));
    }

    private void testProcessingError() {
        Assert.assertThatCode(() -> {
            macroProcessor.process(new List<List<Token>>(new List<>(f.special("#"), f.name("error"))), new Macros());
        }).throwsException(ErrorException.class);
    }
}
