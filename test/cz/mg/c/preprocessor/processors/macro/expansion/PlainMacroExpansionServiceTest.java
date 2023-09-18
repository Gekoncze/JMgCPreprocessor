package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

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
        Macro macro = new Macro(f.name("FOOBAR"), null, new List<>());

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(macro, f.name("FOOBAR"), null);

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>();

        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpandNoParameters() {
        Macro macro = new Macro(
            f.name("FOO"),
            null,
            new List<>(
                f.name("y"),
                f.operator("-"),
                f.name("z"),
                f.operator("+"),
                f.name("x"),
                f.name("x"),
                f.name("x")
            )
        );

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(macro, f.name("FOO"), null);

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>(
            f.name("y"),
            f.operator("-"),
            f.name("z"),
            f.operator("+"),
            f.name("x"),
            f.name("x"),
            f.name("x")
        );

        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpandNoImplementation() {
        Macro macro = new Macro(
            f.name("BAR"),
            new List<>(f.name("x"), f.name("y")),
            new List<>()
        );

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(
            macro,
            f.name("BAR"),
            new List<>(
                new List<>(f.name("oi")),
                new List<>(f.number("7"), f.operator("!"))
            )
        );

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>();

        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpand() {
        Macro macro = new Macro(
            f.name("BARFOO"),
            new List<>(f.name("x"), f.name("y")),
            new List<>(
                f.name("y"),
                f.operator("-"),
                f.name("z"),
                f.operator("+"),
                f.name("x"),
                f.name("x"),
                f.name("x")
            )
        );

        MacroManager macros = new MacroManager(new Macros());
        macros.define(macro);

        MacroCall call = new MacroCall(
            macro,
            f.name("BARFOO"),
            new List<>(
                new List<>(f.name("oi")),
                new List<>(f.number("7"), f.operator("!"))
            )
        );

        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>(
            f.number("7"),
            f.operator("!"),
            f.operator("-"),
            f.name("z"),
            f.operator("+"),
            f.name("oi"),
            f.name("oi"),
            f.name("oi")
        );

        validator.assertEquals(expectedTokens, actualTokens);
    }
}
