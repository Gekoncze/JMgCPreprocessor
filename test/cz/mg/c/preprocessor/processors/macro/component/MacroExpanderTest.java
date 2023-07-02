package cz.mg.c.preprocessor.processors.macro.component;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Test class MacroExpanderTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroExpanderTest.class.getSimpleName() + " ... ");

        MacroExpanderTest test = new MacroExpanderTest();
        test.testSimpleExpansion();

        System.out.println("OK");
    }

    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testSimpleExpansion() {
        Macro foo = new Macro(f.name("FOO"), new List<>(), new List<>(f.name("foo")));
        Macros macros = new Macros();
        macros.define(foo);
        MacroExpander expander = new MacroExpander(macros);
        expander.addTokens(new List<>(f.name("FOO"), f.bracket("("), f.bracket(")")));
        List<Token> actualTokens = expander.getTokens();
        List<Token> expectedTokens = new List<>(f.name("foo"));
        validator.assertEquals(expectedTokens, actualTokens);
    }
}
