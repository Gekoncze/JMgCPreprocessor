package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.entities.macro.system.FileMacro;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.test.Assert;
import cz.mg.token.Token;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenAssertions;

import java.nio.file.Path;

public @Test class MacroExpansionServicesTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroExpansionServicesTest.class.getSimpleName() + " ... ");

        MacroExpansionServicesTest test = new MacroExpansionServicesTest();
        test.testExpandPlainMacro();
        test.testExpandSystemMacro();
        test.testValidation();

        System.out.println("OK");
    }

    private final @Service MacroExpansionServices services = MacroExpansionServices.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenAssertions assertions = TokenAssertions.getInstance();

    private void testExpandPlainMacro() {
        MacroManager macros = new MacroManager(new Macros());
        Macro macro = new Macro(f.word("MACRO"), new List<>(), new List<>(f.word("Aki")));
        macros.define(macro);
        MacroCall call = new MacroCall(macro, f.word("MACRO"), new List<>());
        List<Token> actualTokens = services.expand(call, macros);
        List<Token> expectedTokens = new List<>(f.word("Aki"));
        assertions.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpandSystemMacro() {
        MacroManager macros = new MacroManager(new Macros());
        File file = new File(Path.of("chan"), "");
        FileMacro fileMacro = new FileMacro(file);
        macros.define(fileMacro);
        MacroCall call = new MacroCall(fileMacro, f.word("__FILE__"), null);
        List<Token> actualTokens = services.expand(call, macros);
        List<Token> expectedTokens = new List<>(f.doubleQuote(file.getPath().toAbsolutePath().toString()));
        assertions.assertEquals(expectedTokens, actualTokens);
    }

    private void testValidation() {
        Assert.assertThatCode(() -> {
            MacroManager macros = new MacroManager(new Macros());
            Macro macro = new Macro(f.word("FAIL"), null, null);
            macros.define(macro);
            MacroCall call = new MacroCall(macro, f.word("FAIL"), new List<>());
            services.expand(call, macros);
        }).throwsException(TraceableException.class);
    }
}