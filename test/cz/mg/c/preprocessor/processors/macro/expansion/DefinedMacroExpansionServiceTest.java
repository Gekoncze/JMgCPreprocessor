package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.DefinedMacro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class DefinedMacroExpansionServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + DefinedMacroExpansionServiceTest.class.getSimpleName() + " ... ");

        DefinedMacroExpansionServiceTest test = new DefinedMacroExpansionServiceTest();
        test.testExpand();

        System.out.println("OK");
    }

    private final @Service DefinedMacroExpansionService service = DefinedMacroExpansionService.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testExpand() {
        MacroManager macros = new MacroManager(new Macros());
        DefinedMacro definedMacro = new DefinedMacro();
        macros.define(definedMacro);

        MacroCall call = new MacroCall(
            definedMacro,
            new NameToken("defined", 10),
            new List<List<Token>>(new List<>(new NameToken("FOOBAR", 20)))
        );

        validator.assertEquals(
            new List<>(new NumberToken("0", 10)),
            service.expand(macros, call)
        );

        macros.define(new Macro(new NameToken("FOOBAR", 5), null, new List<>()));

        validator.assertEquals(
            new List<>(new NumberToken("1", 10)),
            service.expand(macros, call)
        );
    }
}
