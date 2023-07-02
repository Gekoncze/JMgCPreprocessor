package cz.mg.c.preprocessor.processors.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Test class MacroProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroProcessorTest.class.getSimpleName() + " ... ");

        MacroProcessorTest test = new MacroProcessorTest();
        test.testMacroCallRecursion();

        System.out.println("OK");
    }

    private final @Service MacroProcessor macroProcessor = MacroProcessor.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testMacroCallRecursion() {
        Macro foo = new Macro(f.name("FOO"), new List<>(), new List<>(f.name("BAR"), f.bracket("("), f.bracket(")")));
        Macro bar = new Macro(f.name("BAR"), new List<>(), new List<>(f.name("FOO"), f.bracket("("), f.bracket(")")));
        Macros macros = new Macros();
        macros.define(foo);
        macros.define(bar);
        List<List<Token>> lines = new List<>();
        lines.addLast(new List<>(f.name("FOO"), f.special("("), f.special(")")));
        List<Token> actualTokens = macroProcessor.process(lines, macros);
        List<Token> expectedTokens = new List<>(f.name("FOO"), f.bracket("("), f.bracket(")"));
        validator.assertEquals(expectedTokens, actualTokens);
    }
}
