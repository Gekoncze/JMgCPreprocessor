package cz.mg.c.preprocessor.processors.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.preprocessor.processors.macro.exceptions.ErrorException;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenValidator;

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
        macros.getDefinitions().addLast(new Macro(f.word("FOO"), null, new List<>()));
        macros.getDefinitions().addLast(new Macro(f.word("DELETE_ME_NOW"), null, new List<>()));

        List<List<Token>> lines = new List<>(
            new List<>(f.number("1")),
            new List<>(f.special("#"), f.word("ifdef"), f.word("FOO")),
            new List<>(f.number("2")),
            new List<>(f.special("#"), f.word("ifdef"), f.word("BAR")),
            new List<>(f.number("3")),
            new List<>(f.special("#"), f.word("else")),
            new List<>(f.number("4")),
            new List<>(f.special("#"), f.word("endif")),
            new List<>(f.number("5")),
            new List<>(f.special("#"), f.word("else")),
            new List<>(f.number("6")),
            new List<>(f.special("#"), f.word("endif")),
            new List<>(f.number("7")),
            new List<>(f.special("#"), f.word("undef"), f.word("DELETE_ME_NOW"))
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

        Assert.assertEquals(1, macros.getDefinitions().count());
        Assert.assertEquals("FOO", macros.getDefinitions().getFirst().getName().getText());
    }

    private void testProcessingError() {
        Assert.assertThatCode(() -> {
            macroProcessor.process(new List<List<Token>>(new List<>(f.special("#"), f.word("error"))), new Macros());
        }).throwsException(ErrorException.class);
    }
}
