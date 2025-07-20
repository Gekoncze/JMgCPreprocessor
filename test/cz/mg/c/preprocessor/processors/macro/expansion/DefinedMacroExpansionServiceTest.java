package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.entities.macro.system.DefinedMacro;
import cz.mg.collections.list.List;
import cz.mg.token.Token;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.NumberToken;
import cz.mg.token.test.TokenAssertions;

public @Test class DefinedMacroExpansionServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + DefinedMacroExpansionServiceTest.class.getSimpleName() + " ... ");

        DefinedMacroExpansionServiceTest test = new DefinedMacroExpansionServiceTest();
        test.testExpand();

        System.out.println("OK");
    }

    private final @Service DefinedMacroExpansionService service = DefinedMacroExpansionService.getInstance();
    private final @Service TokenAssertions assertions = TokenAssertions.getInstance();

    private void testExpand() {
        MacroManager macros = new MacroManager(new Macros());
        DefinedMacro definedMacro = new DefinedMacro();
        macros.define(definedMacro);

        MacroCall call = new MacroCall(
            definedMacro,
            new WordToken("defined", 10),
            new List<List<Token>>(new List<>(new WordToken("FOOBAR", 20)))
        );

        assertions.assertEquals(
            new List<>(new NumberToken("0", 10)),
            service.expand(macros, call)
        );

        macros.define(new Macro(new WordToken("FOOBAR", 5), null, new List<>()));

        assertions.assertEquals(
            new List<>(new NumberToken("1", 10)),
            service.expand(macros, call)
        );
    }
}