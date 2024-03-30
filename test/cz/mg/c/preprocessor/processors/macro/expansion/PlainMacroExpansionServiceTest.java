package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class PlainMacroExpansionServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + PlainMacroExpansionServiceTest.class.getSimpleName() + " ... ");

        PlainMacroExpansionServiceTest test = new PlainMacroExpansionServiceTest();
        test.testExpandNoParametersNoImplementation();
        test.testExpandNoParameters();
        test.testExpandNoImplementation();
        test.testExpand();

        System.out.println("OK");
    }

    private final @Service PlainMacroExpansionService service = PlainMacroExpansionService.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testExpandNoParametersNoImplementation() {
        Macro macro = new Macro(f.word("FOOBAR"), null, new List<>());

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(macro, f.word("FOOBAR"), null);

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>();

        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpandNoParameters() {
        Macro macro = new Macro(
            f.word("FOO"),
            null,
            new List<>(
                f.word("y"),
                f.symbol("-"),
                f.word("z"),
                f.symbol("+"),
                f.word("x"),
                f.word("x"),
                f.word("x")
            )
        );

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(macro, f.word("FOO"), null);

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>(
            f.word("y"),
            f.symbol("-"),
            f.word("z"),
            f.symbol("+"),
            f.word("x"),
            f.word("x"),
            f.word("x")
        );

        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpandNoImplementation() {
        Macro macro = new Macro(
            f.word("BAR"),
            new List<>(f.word("x"), f.word("y")),
            new List<>()
        );

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(
            macro,
            f.word("BAR"),
            new List<>(
                new List<>(f.word("oi")),
                new List<>(f.number("7"), f.symbol("!"))
            )
        );

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>();

        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpand() {
        Macro macro = new Macro(
            f.word("BARFOO"),
            new List<>(f.word("x"), f.word("y")),
            new List<>(
                f.word("y"),
                f.symbol("-"),
                f.word("z"),
                f.symbol("+"),
                f.word("x"),
                f.word("x"),
                f.word("x")
            )
        );

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(
            macro,
            f.word("BARFOO"),
            new List<>(
                new List<>(f.word("oi")),
                new List<>(f.number("7"), f.symbol("!"))
            )
        );

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>(
            f.number("7"),
            f.symbol("!"),
            f.symbol("-"),
            f.word("z"),
            f.symbol("+"),
            f.word("oi"),
            f.word("oi"),
            f.word("oi")
        );

        validator.assertEquals(expectedTokens, actualTokens);
    }
}
